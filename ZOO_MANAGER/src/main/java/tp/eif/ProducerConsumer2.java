package tp.eif;

	import org.apache.camel.CamelContext;
	import org.apache.camel.Exchange;
	import org.apache.camel.Expression;
	import org.apache.camel.ProducerTemplate;
	import org.apache.camel.builder.RouteBuilder;
	import org.apache.camel.http.common.HttpOperationFailedException;
	import org.apache.camel.impl.DefaultCamelContext;
	import org.apache.camel.model.dataformat.JaxbDataFormat;
	import org.apache.camel.model.dataformat.JsonLibrary;
	import org.apache.log4j.BasicConfigurator;

	import tp.model.Animal;

	import javax.xml.bind.JAXBContext;
	import java.util.*;

	/**
	 * Created by root on 10/05/17.
	 */
	public class ProducerConsumer2 {

	    public static void findCoord() throws Exception {
	        // Configure le logger par défaut
	        BasicConfigurator.configure();

	        // Contexte Camel par défaut
	        CamelContext context = new DefaultCamelContext();

	        // Recherche d'un animal par son nom
	        RouteBuilder routeBuilderSwitch = new RouteBuilder() {

	            @Override
	            public void configure() throws Exception {
	                // Start MyServiceController before
	                from("direct:testInstance")
	                        .setHeader(Exchange.HTTP_METHOD,constant("POST"))
	                        .to("http://127.0.0.1:8025/animals/findByName/")
	                        .unmarshal().json(JsonLibrary.Jackson)
	                        .setBody(simple("${body[cage]}"))
	                        .to("http://127.0.0.1:8025/cage/findByName/")
	                        .unmarshal().json(JsonLibrary.Jackson)
	                        .setHeader("longitude", simple("${body[position][longitude]}"))
	                        .setHeader("latitude", simple("${body[position][latitude]}"))
	                        .log("${header.latitude} ${header.longitude}");
	            }
	        };
	        routeBuilderSwitch.addRoutesToCamelContext(context);


	        // démarrer le contexte 
	        context.start();

	        // un producteur 
	        ProducerTemplate pt = context.createProducerTemplate();

	        String message = "Canine";

	        Scanner scanner = new Scanner(System.in);
	        while (true) {
	            // Enter the name of an animal or close to exit
	            message = scanner.nextLine();
	            if (message.equals("exit")) break;
	            pt.sendBody("direct:testInstance", message);
	        }
	    }

	    public static void chercherAvecPlsInstance() throws Exception {
	        // Configure le logger par défaut
	        BasicConfigurator.configure();

	        // Contexte Camel par défaut
	        CamelContext context = new DefaultCamelContext();

	        // recherche d'un animal par son nom
	        RouteBuilder routeBuilderSwitch = new RouteBuilder() {

	            @Override 
	            public void configure() throws Exception {
	            // Start MyServiceController before

	            String[] uris = {
	                    "http://127.0.0.1:8025/animals/findByName/",
	                    "http://127.0.0.1:8087/animals/findByName/"
	            };
	            
	            //gerer l'agregation s'il le trouve pas dans le premiers va le chercher dans le deuxieme
	            //s'il le trouve pas dans les deux il affiche un message animal n'existe pas
	            from("direct:testInstance")
	                .setHeader("searched", simple("${body}"))
	                .setHeader(Exchange.HTTP_METHOD,constant("POST"))
	                .doTry()
	                    .setBody(header("searched"))
	                    .to("http://127.0.0.1:8025/animals/findByName/")
	                    .unmarshal().json(JsonLibrary.Jackson)
	                    .log("${body}")
	                .doCatch(HttpOperationFailedException.class)
	                    .doTry()
	                        .setBody(header("searched"))
	                        .to("http://127.0.0.1:8087/animals/findByName/")
	                        .unmarshal().json(JsonLibrary.Jackson)
	                        .log("${body}")
	                    .doCatch(HttpOperationFailedException.class)
	                        .log("l'animal n'existe pas")
	                    .end()
	                .end()

	            ;
	            }
	        };
	        routeBuilderSwitch.addRoutesToCamelContext(context);


	        //démarrer le contexte 
	        context.start();

	        // crée un producteur
	        ProducerTemplate pt = context.createProducerTemplate();

	        String message = "Canine";

	        Scanner scanner = new Scanner(System.in);
	        while (true) {
	            // le nom animal chercher
	            message = scanner.nextLine();
	            if (message.equals("exit")) break;
	            pt.sendBody("direct:testInstance", message);
	        }
	    }

	    public static void main(String[] args) throws Exception {
	        chercherAvecPlsInstance();
	    }

	
	
}
