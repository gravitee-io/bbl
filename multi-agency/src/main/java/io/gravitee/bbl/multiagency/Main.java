/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.bbl.multiagency;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * @author Nicolas GERAUD (nicolas at graviteesource.com)
 * @author GraviteeSource Team
 */
public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        Route get = router.route().method(HttpMethod.GET).produces("application/json");
        get.handler(routingContext -> {
            JsonArray content = new JsonArray(
                    "[\n" +
                            "  {\n" +
                            "    \"nom\": \"AXA Assurance Lille\",\n" +
                            "    \"conseiller\": \"Pascal Coue\",\n" +
                            "    \"metier\": \"Agent Général d'assurance AXA France\",\n" +
                            "    \"adresse\": \"124b Rue Faubourg De Roubaix 59000 Lille\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"nom\": \"AXA Assurance Lille\",\n" +
                            "    \"conseiller\": \"Patricia Mariez\",\n" +
                            "    \"metier\": \"Agent Mandataire AXA Epargne et Protection\",\n" +
                            "    \"adresse\": \"124b Rue Faubourg De Roubaix 59000 Lille\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"nom\": \"AXA Assurance Lille\",\n" +
                            "    \"conseiller\": \"Costa Et Leclercq\",\n" +
                            "    \"metier\": \"Agents Généraux d'assurance AXA France\",\n" +
                            "    \"adresse\": \"89 Bd De La Liberte 59000 Lille\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"nom\": \"AXA Assurance Lille\",\n" +
                            "    \"conseiller\": \"Bruno Acloque\",\n" +
                            "    \"metier\": \"Agent Général d'assurance AXA France\",\n" +
                            "    \"adresse\": \"53 Boulevard Carnot 59800 Lille\"\n" +
                            "  }\n" +
                            "]"
            );

            vertx.setTimer(
                    500,
                    id -> routingContext.response().
                            putHeader("content-type", "application/json").
                            setStatusCode(200).
                            end(content.encodePrettily())
            );
        });

//        Route post = router.route().method(HttpMethod.POST);
//        post.handler(routingContext -> {
//            HttpServerRequest request = routingContext.request();
//            HttpServerResponse response = routingContext.response();
//
//            int statusCode = request.getParam("statusCode") == null
//                    ? 200
//                    : Integer.valueOf(request.getParam("statusCode"));
//
//            response.setStatusCode(statusCode);
//
//            request.headers().entries().stream()
//                    .filter( entry -> !"host".equalsIgnoreCase(entry.getKey()) )
//                    .forEach( entry -> response.putHeader(entry.getKey(), entry.getValue()) );
//
//            request.handler(response::write);
//            request.endHandler(aVoid -> response.end());
//        });

        int port = 8080;
        if (args.length > 0) {
            try{
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        server.requestHandler(router::accept).listen(port);
        System.out.println("Server listening on port " + port);
    }
}
