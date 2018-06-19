package mx.unam.dgpe.sample.controller;

import java.util.HashMap;
import java.util.Map;
import java.math.BigInteger;
import org.apache.log4j.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MyController extends AbstractVerticle {
    private static final Logger logger = Logger.getLogger(MyController.class);
    private static String pba;
    
    public void start(Future<Void> fut) {
        logger.info("Inicializando Vertical");
        Router router = Router.router(vertx);
        //router.route("/*").handler(StaticHandler.create("assets")); // para invocar asi: http://localhost:8080/index.html
        // el directorio "upload-folder" será creado en la misma ubicación que el jar fue ejecutado
        router.route().handler(BodyHandler.create().setUploadsDirectory("upload-folder"));
        router.get("/api/primero").handler(this::primero);
        router.post("/api/segundo").handler(this::segundo);
        router.get("/api/sumar").handler(this::sumar);
        router.get("/api/calcula").handler(this::calcula);

        
        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx.createHttpServer().requestHandler(router::accept).listen(
                config().getInteger("http.port", 8080), result -> {
                    if (result.succeeded()) {
                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                });        
        pba = System.getenv("PBA");
        logger.info("Vertical iniciada !!!");
    }  
    private void primero(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        String mode = request.getParam("mode");
        String jsonResponse = procesa(mode, request);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }

   private void sumar(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        //String mode   = request.getParam("mode");
        String valor1 = request.getParam("valor1");
        String valor2 = request.getParam("valor2");
        String jsonResponse = procesaSumar(valor1,valor2,request);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }
private void calcula(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();

        String xvalor = request.getParam("valor");
        int valor = Integer.parseInt(xvalor);

        String jsonResponse = procesaCalcula(valor,request);

        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }    
    private void segundo(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();       
        String decoded = routingContext.getBodyAsString();
        String jsonResponse = procesa(decoded, request);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }

    private String procesa(String decoded, HttpServerRequest request) {
        Map<String, String> srv = new HashMap<>();
	     srv.put("Current Node IP", request.localAddress().host());
	     srv.put("Caller IP", request.remoteAddress().host());

             srv.put("Absolute url", request.absoluteURI());
             srv.put("path",  request.path());
             srv.put("query", request.query());

             srv.put("uri", request.uri());


        Map<String, String> autos = new HashMap<>();
        
        autos.put("primero", "Ferrari");
        autos.put("segundo", "Lamborgini");
        autos.put("tercero", "Bugatti");
        
        Map<Object, Object> info = new HashMap<>();

        info.put("decoded", decoded);
        info.put("nombre", "gustavo");
        info.put("edad", "21");
        info.put("autos", autos);
        info.put("variable",pba);
        return Json.encodePrettily(info);
    }

    private String procesaSumar(String valor1, String valor2, HttpServerRequest request) {
	// agrega Datos del Servidor
        Map<String, String> srv = new HashMap<>();

             srv.put("Current Node IP", request.localAddress().host());
             srv.put("Caller IP", request.remoteAddress().host());

             srv.put("Absolute url", request.absoluteURI());
             srv.put("path",  request.path());
             srv.put("query", request.query());

             srv.put("uri", request.uri());



        // Realiza la operacion de suma
        
        int resultado = 0;
        int xValor1 = Integer.parseInt(valor1);
        int xValor2 = Integer.parseInt(valor2);

        resultado = xValor1 + xValor2;
        String xResultado =  String.valueOf(resultado);

        Map<String, String> autos = new HashMap<>();
        autos.put("primero", "Ferrari");
        autos.put("segundo", "Lamborgini");
        autos.put("tercero", "Bugatti");

        Map<Object, Object> info = new HashMap<>();
        info.put("valor2",valor2);
        info.put("variable",pba);
        info.put("valor1",valor1);
        info.put("suma",xResultado);
	info.put("servicio",srv);

        //info.put("decoded", decoded);
        return Json.encodePrettily(info);
    }

 private String procesaCalcula(int numero, HttpServerRequest request) {
        int resultado = 0;

        if (numero < 0){
           resultado = 0;    
        }         
        if (numero <=1){
           resultado = 1;    
        }  
	double digits = 0;
	for(int i=2; i<=numero; i++){
	   digits += Math.log10(i);
	}

        resultado = (int) (Math.floor(digits)) + 1;

        Map<Object, Object> info = new HashMap<>();
        info.put("valor",numero);
        info.put("Digitosxx",resultado);

        return Json.encodePrettily(info);
    }


}
