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

        JsonArray agencies =  new JsonArray();
        agencies.add( new JsonObject(
                "{" +
                        "\"nom\": \"AXA Assurance Lille\"," +
                        "\"conseiller\": \"Pascal Coue\"," +
                        "\"metier\": \"Agent Général d'assurance AXA France\"," +
                        "\"adresse\": \"124b Rue Faubourg De Roubaix 59000 Lille\"" +
                        "}"
        ));
        agencies.add( new JsonObject(
                "{" +
                        "\"nom\": \"AXA Assurance Lille\"," +
                        "\"conseiller\": \"Patricia Mariez\"," +
                        "\"metier\": \"Agent Mandataire AXA Epargne et Protection\"," +
                        "\"adresse\": \"124b Rue Faubourg De Roubaix 59000 Lille\"" +
                        "}"
        ));
        agencies.add( new JsonObject(
                "{" +
                        "\"nom\": \"AXA Assurance Lille\"," +
                        "\"conseiller\": \"Costa Et Leclercq\"," +
                        "\"metier\": \"Agents Généraux d'assurance AXA France\"," +
                        "\"adresse\": \"89 Bd De La Liberte 59000 Lille\"" +
                        "}"
        ));
        agencies.add( new JsonObject(
                "{" +
                        "\"nom\": \"AXA Assurance Lille\"," +
                        "\"conseiller\": \"Bruno Acloque\"," +
                        "\"metier\": \"Agent Général d'assurance AXA France\"," +
                        "\"adresse\": \"53 Boulevard Carnot 59800 Lille\"" +
                        "}"
        ));

        int port = 8080;
        int agencyId =-1;
        if (args.length > 0) {
            try{
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (port != 8080) {
            agencyId = port % agencies.size();
        }

        Route get = router.route().method(HttpMethod.GET).produces("application/json");
        final int finalagencyid = agencyId;
        final int latency = finalagencyid == -1 ? 500 : 1;

        get.handler(routingContext -> {
            vertx.setTimer(
                    latency,
                    id -> routingContext.response().
                            putHeader("content-type", "application/json").
                            setStatusCode(200).
                            end( finalagencyid == -1 ?
                                    agencies.encodePrettily():
                                    agencies.getJsonObject(finalagencyid).encodePrettily()
                            )
            );
        });

        Route post = router.route().method(HttpMethod.POST);
        post.handler(routingContext ->
                routingContext.response().
                        setStatusCode(201).
                        end());


        server.requestHandler(router::accept).listen(port);
        System.out.println("Server listening on port " + port);
    }
}
