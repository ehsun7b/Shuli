package com.ehsunbehravesh.shuli.application;

import com.ehsunbehravesh.shuli.exception.ApplicationException;
import com.ehsunbehravesh.shuli.resource.Resource;
import com.ehsunbehravesh.shuli.resource.service.Get;
import com.ehsunbehravesh.shuli.resource.service.Path;
import com.ehsunbehravesh.shuli.resource.service.Post;
import com.ehsunbehravesh.shuli.resource.service.Produces;
import com.ehsunbehravesh.shuli.resource.service.Service;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

/**
 *
 * @author Ehsun Behravesh
 */
public abstract class Application {

  private static final Logger log = LogManager.getLogger(Application.class);

  private static final int DEFAULT_PORT = 6265;
  private static final int DEFAULT_BUFFER_SIZE = 2048;
  private static final String DEFAULT_CONTEXT = "/";
  private static final String DEFAULT_CONFIG_FILENAME = "shuli.properties";

  protected Properties config;
  protected HashMap<String, Class<? extends Service>> services;
  protected HashMap<String, Resource> resources;
  protected int port;
  protected String context;
  protected int bufferSize;

  public Application() {
    resources = new HashMap<>();
    services = new HashMap<>();
  }

  protected void setup() throws Exception {
    // to be filled by the application developer
  }

  private void init() throws ApplicationException, IOException {
    if (config == null) {
      config = loadConfigFromClasspath();
    }

    if (port == 0) {
      try {
        port = Integer.parseInt(config.getProperty("port"));
      } catch (NumberFormatException | NullPointerException ex) {
        port = DEFAULT_PORT;
      }
    }

    if (context == null) {
      context = config.getProperty("context");
      if (context == null) {
        context = DEFAULT_CONTEXT;
      }
    }

    if (!context.startsWith("/")) {
      throw new ApplicationException("context must starts with /", null);
    }

    if (bufferSize == 0) {
      try {
        bufferSize = Integer.parseInt(config.getProperty("buffer_size"));
      } catch (NumberFormatException | NullPointerException ex) {
        bufferSize = DEFAULT_BUFFER_SIZE;
      }
    }

    loadServices();
    _showConfigAndResources();
  }

  protected void loadServices() throws ApplicationException {
    String packag = config.getProperty(Config.SCAN_PACKAGE);

    if (packag == null) {
      log.warn(MessageFormat.format("{0} is null. You can set it in your configuration properties.", Config.SCAN_PACKAGE));
    } else {

      Reflections reflections = new Reflections(packag);

      Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Path.class);

      for (Class<?> clasz : serviceClasses) {
        if (Service.class.isAssignableFrom(clasz)) {
          Class<? extends Service> serviceClass = (Class<? extends Service>) clasz;
          Path path = serviceClass.getAnnotation(Path.class);
          String pathValue = path.value();

          if (!pathValue.startsWith("/")) {
            pathValue = "/".concat(pathValue);
          }

          services.put(pathValue, serviceClass);
        } else {
          throw new ApplicationException(MessageFormat.format(
                  "Class {0} must extends {1}",
                  clasz.getCanonicalName(),
                  Service.class.getCanonicalName()), null);
        }
      }
    }
  }

  private Properties loadConfigFromClasspath() throws IOException, ApplicationException {
    InputStream configResource = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILENAME);

    if (configResource == null) {
      throw new ApplicationException(MessageFormat.format(
              "File {0} not found in the classpath. You may set the config manually in the setup method.",
              Application.DEFAULT_CONFIG_FILENAME), null);
    }

    Properties prop = new Properties();
    prop.load(configResource);

    return prop;
  }

  public void start() throws ApplicationException, IOException {
    try {
      setup();
      init();
    } catch (Exception ex) {
      throw new ApplicationException(ex.getMessage(), ex);
    }

    InetSocketAddress addrress = new InetSocketAddress(port);
    HttpServer server = HttpServer.create(addrress, 0);

    server.createContext(context, (HttpExchange exchange) -> {
      try {
        handler(exchange);
      } catch (ApplicationException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        log.fatal(ex);
        ex.printStackTrace();
      }
    });
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();

    log.info(MessageFormat.format("Shuli started: {0}:{1}{2}", new Object[]{addrress.getHostName(), String.valueOf(addrress.getPort()), context}));
    log.info("Press CTRL+C to stop the server.");
  }

  private void handler(HttpExchange exchange) throws IOException, ApplicationException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    URI requestURI = exchange.getRequestURI();
    String path = requestURI.getPath().toLowerCase();

    if (context.length() > 1) {
      if (path.equals(context)) {
        path = path.concat("/");
      }

      if (path.startsWith(context)) {
        path = path.substring(context.length());
      }
    }

    Resource resource = resources.get(path);

    if (resource != null) {
      if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        response(exchange, resource);
      } else {
        response405(exchange);
      }
    } else {
      String[] pathParts = extractServiceAndMethodPaths(path);

      String servicePath = pathParts[0];
      String methodPath = pathParts[1];

      log.debug(MessageFormat.format("Service: class {0} method {1}", servicePath, methodPath));

      Class<? extends Service> service = services.get(servicePath);

      if (service != null) {
        String method = exchange.getRequestMethod().toLowerCase();
        processRequest(exchange, method, service, methodPath);
      } else {
        response404(exchange);
      }
    }
  }

  private void response404(HttpExchange exchange) throws IOException {
    Headers responseHeaders = exchange.getResponseHeaders();
    responseHeaders.set("Content-Type", "text/html");
    exchange.sendResponseHeaders(404, 0);

    try (OutputStream os = exchange.getResponseBody()) {
      os.write("Page not found!".getBytes());
    }

    exchange.getResponseBody().close();
  }

  private void response405(HttpExchange exchange) throws IOException {
    Headers responseHeaders = exchange.getResponseHeaders();
    responseHeaders.set("Content-Type", "text/html");
    exchange.sendResponseHeaders(405, 0);

    try (OutputStream os = exchange.getResponseBody()) {
      os.write("Method not allowed!".getBytes());
    }

    exchange.getResponseBody().close();
  }

  private void response(HttpExchange exchange, Resource resource) throws IOException {
    Headers responseHeaders = exchange.getResponseHeaders();
    responseHeaders.set("Content-Type", resource.getContentType());
    exchange.sendResponseHeaders(200, 0);

    try (OutputStream os = exchange.getResponseBody()) {
      URLConnection connection = resource.getContentUrl().openConnection();
      try (InputStream is = connection.getInputStream()) {
        byte[] buffer = new byte[bufferSize];
        int len;

        while ((len = is.read(buffer)) > 0) {
          os.write(buffer, 0, len);
        }
      }
    }

    exchange.getResponseBody().close();
  }

  private void processRequest(HttpExchange exchange, String method,
          Class<? extends Service> service, String methodPath)
          throws ApplicationException, IOException, InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {
    Method serviceMethod = getServiceMethod(service, methodPath, method);
    log.debug(MessageFormat.format("Method is {0}", serviceMethod));

    Produces producesAnnotation = serviceMethod.getAnnotation(Produces.class);
    String produces = null;

    if (producesAnnotation != null) {
      produces = producesAnnotation.value();
    }

    Service serviceInstance = service.newInstance();
    Object result = serviceMethod.invoke(serviceInstance);

    Headers responseHeaders = exchange.getResponseHeaders();
    if (produces != null) {
      responseHeaders.set("Content-Type", produces);
    }
    exchange.sendResponseHeaders(200, 0);

    try (OutputStream os = exchange.getResponseBody()) {
      String toString = result.toString();
      os.write(toString.getBytes("UTF-8"));
    }

    exchange.getResponseBody().close();
  }

  private Method getServiceMethod(Class<? extends Service> service, String methodPath, String method) throws ApplicationException {
    Method[] methods = service.getMethods();

    Class annotationClass;
    if (method.equalsIgnoreCase("get")) {
      annotationClass = Get.class;
    } else if (method.equalsIgnoreCase("post")) {
      annotationClass = Post.class;
    } else if (method.equalsIgnoreCase("head")) {
      throw new ApplicationException("Method head is not supported yet!", null);
    } else if (method.equalsIgnoreCase("option")) {
      throw new ApplicationException("Method option is not supported yet!", null);
    } else {
      throw new ApplicationException(MessageFormat.format("Unknown http method {0}", method), null);
    }

    for (Method method1 : methods) {
      Annotation annotation = method1.getAnnotation(annotationClass);

      if (annotation != null) {
        return method1;
      }
    }

    return null;
  }

  private String[] extractServiceAndMethodPaths(String path) {
    if (path.indexOf("/", path.indexOf("/") + 1) > 1) {
      String servicePath = path.substring(0, path.indexOf("/", path.indexOf("/") + 1));
      String methodPath = path.substring(path.indexOf("/", path.indexOf("/") + 1));

      return new String[]{servicePath, methodPath};
    } else {
      return new String[]{path, ""};
    }
  }

  private void _showConfigAndResources() {
    //System.out.println(config);
    log.debug(config);

    Set<String> keySet = resources.keySet();
    Iterator<String> iterator = keySet.iterator();

    while (iterator.hasNext()) {
      log.debug(MessageFormat.format("resource path: ", iterator.next()));
    }
  }

  public void loadConfig(InputStream inputStream) throws IOException {
    config = new Properties();
    config.load(inputStream);
  }

  public Properties getConfig() {
    return config;
  }

  public void setConfig(Properties config) {
    this.config = config;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public HashMap<String, Resource> getResources() {
    return resources;
  }

  public void setResources(HashMap<String, Resource> resources) {
    this.resources = resources;
  }

  public void addResource(String path, Resource resource) throws ApplicationException {
    if (!path.startsWith("/")) {
      throw new ApplicationException("path must starts with /", null);
    }

    resources.put(path, resource);
  }

  public Resource getResource(String path) {
    return resources.get(path);
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) throws ApplicationException {
    if (!context.startsWith("/")) {
      throw new ApplicationException("context must starts with /", null);
    }

    this.context = context;
  }

  public int getBufferSize() {
    return bufferSize;
  }

  public void setBufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
  }

}
