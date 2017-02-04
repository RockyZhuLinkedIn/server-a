package com.rockyzhu.myserver.dev;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * The DevServer class contains the main() method to enable running the servlet in debug mode.
 *
 * @author Rocky Zhu
 */
public class DevServer {

  private static final String WEB_APP_PATH = "file:/Users/hozhu/work/components/myserver-backend_trunk/myserver/myserver-backend-war/src/main/webapp";
  private static final String WEB_XML_PATH = WEB_APP_PATH + "/WEB-INF/web.xml";
  private static final String CONTEXT_PATH = "/myserver-backend";

  // looks like we need internet access to run it? double check it
  public static void main(String[] args) throws Exception {
    Server server = new Server();

    WebAppContext webApp = new WebAppContext(WEB_APP_PATH, CONTEXT_PATH);
    webApp.setDescriptor(WEB_XML_PATH);
    webApp.setParentLoaderPriority(true);

    HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(new HttpConfiguration());
    ServerConnector connector = new ServerConnector(server, httpConnectionFactory);
    connector.setHost("0.0.0.0");
    connector.setPort(7077);

    server.setHandler(webApp);
    server.addConnector(connector);

    // Problem:
    //     Exception in thread "main" java.lang.NoSuchMethodError: org.eclipse.jetty.util.resource.Resource.isAlias()Z
    // Cause ofthe problem:
    //     kafka library uses:    jetty-util-9.2.15.v20160210.jar and
    //     I also manually set:   jetty-util-9.3.10.v20160621.jar
    //     They both show up in External Libraries.
    // Solution:
    //     Remove the 9.2.15 one
    server.start();
    ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    service.scheduleAtFixedRate(() -> {
      if (server.isStarted()) {
        WebAppContext context = ((WebAppContext) server.getHandler());
        if (context.isAvailable()) {
          System.out.println(READY);
          System.out.println(String.format("Ready on: http://%s:%s%s", "0.0.0.0", "7071", CONTEXT_PATH));
          System.out.println("\n\n");
        } else {
          System.out.println("Server started but web app context unavailable. Reason: " + context.getUnavailableException());
        }
        service.shutdownNow();
      }
    }, 0, 100, TimeUnit.MILLISECONDS);
    server.join();
  }

  private static String READY = "\n\n"
      + "RRRRRRRRRRRRRRRRR   EEEEEEEEEEEEEEEEEEEEEE               AAA               DDDDDDDDDDDDD       YYYYYYY       YYYYYYY\n"
      + "R::::::::::::::::R  E::::::::::::::::::::E              A:::A              D::::::::::::DDD    Y:::::Y       Y:::::Y\n"
      + "R::::::RRRRRR:::::R E::::::::::::::::::::E             A:::::A             D:::::::::::::::DD  Y:::::Y       Y:::::Y\n"
      + "RR:::::R     R:::::REE::::::EEEEEEEEE::::E            A:::::::A            DDD:::::DDDDD:::::D Y::::::Y     Y::::::Y\n"
      + "  R::::R     R:::::R  E:::::E       EEEEEE           A:::::::::A             D:::::D    D:::::DYYY:::::Y   Y:::::YYY\n"
      + "  R::::R     R:::::R  E:::::E                       A:::::A:::::A            D:::::D     D:::::D  Y:::::Y Y:::::Y   \n"
      + "  R::::RRRRRR:::::R   E::::::EEEEEEEEEE            A:::::A A:::::A           D:::::D     D:::::D   Y:::::Y:::::Y    \n"
      + "  R:::::::::::::RR    E:::::::::::::::E           A:::::A   A:::::A          D:::::D     D:::::D    Y:::::::::Y     \n"
      + "  R::::RRRRRR:::::R   E:::::::::::::::E          A:::::A     A:::::A         D:::::D     D:::::D     Y:::::::Y      \n"
      + "  R::::R     R:::::R  E::::::EEEEEEEEEE         A:::::AAAAAAAAA:::::A        D:::::D     D:::::D      Y:::::Y       \n"
      + "  R::::R     R:::::R  E:::::E                  A:::::::::::::::::::::A       D:::::D     D:::::D      Y:::::Y       \n"
      + "  R::::R     R:::::R  E:::::E       EEEEEE    A:::::AAAAAAAAAAAAA:::::A      D:::::D    D:::::D       Y:::::Y       \n"
      + "RR:::::R     R:::::REE::::::EEEEEEEE:::::E   A:::::A             A:::::A   DDD:::::DDDDD:::::D        Y:::::Y       \n"
      + "R::::::R     R:::::RE::::::::::::::::::::E  A:::::A               A:::::A  D:::::::::::::::DD      YYYY:::::YYYY    \n"
      + "R::::::R     R:::::RE::::::::::::::::::::E A:::::A                 A:::::A D::::::::::::DDD        Y:::::::::::Y    \n"
      + "RRRRRRRR     RRRRRRREEEEEEEEEEEEEEEEEEEEEEAAAAAAA                   AAAAAAADDDDDDDDDDDDD           YYYYYYYYYYYYY    \n"
      + "                                                                                                                    \n"
      + "                                                                                                                    ";
}