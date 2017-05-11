package tp.eif;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ValueBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.BasicConfigurator;

import java.util.Scanner;

import static java.lang.System.in;

public class ProducerConsumer {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(in);
        BasicConfigurator.configure();
        CamelContext context = new DefaultCamelContext();
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct:consumer-1").to("log:affiche-1-log");
                from("direct:consumer-2").to("file:messages");
                from("direct:consumer-all")
                        .choice()
                        .when(header("destinataire").contains("écrire"))
                            .to("direct:consumer-2")
                        .otherwise()
                            .to("direct:consumer-1");

            }
        };
        routeBuilder.addRoutesToCamelContext(context);
        context.start();
        ProducerTemplate pt = context.createProducerTemplate();
        while (scanner.hasNext()) {
            String text = scanner.next();
            if (text.equals("exit")) {
            	pt.stop();
                break;
            }
            if (text.substring(0, 1).equals("w")) {
                pt.sendBodyAndHeader("direct:consumer-all", scanner.next(), "destinataire", "écrire");
            } else if (text.equals("animal")) {
                System.out.println("entrez un endpoint");
                String endpoint = scanner.next();
                endpoint = "direct:"+endpoint;
                System.out.println("entrez le nom de l'animal");
                addBasicConsumer(context,endpoint,"http://127.0.0.1:8025/animals/findByName/"+scanner.next());
              //si l'animal n'existe pas retourne animal n'existe pas
                try {
                	pt.sendBody(endpoint,"");
				} catch (Exception e) {
					System.out.println("animal n'existe pas");
				}
                
            } else if (text.equals("geonames")) {
            	addBasicConsumer(context,"direct:geonames","http://api.geonames.org/search?q=Biotropica&username=demo");
                pt.sendBody("direct:geonames", "");		
            } else {
                pt.sendBody("direct:consumer-all", text);
            }

        }

    }

    //créer un consomateur simple et l'ajout au context
    private static void addBasicConsumer(CamelContext context, String uri, String to) throws Exception {
        final String furi = uri;
        final String fto = to;
        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from(furi)
                        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .to(fto)
                        .log("response received : ${body}");
            }
        };

        // On ajoute la route au contexte
        routeBuilder.addRoutesToCamelContext(context);
    }
}