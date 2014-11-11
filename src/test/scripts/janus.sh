#!/bin/bash
clear
now=$(date +"%T")

curl -k \
-v \
--user "engine:engineblock" \
-X POST \
-H "Content-Type: application/json" \
-d '{"name":"http://saml-'${now}'.ps-ui-test.qalab.geant.net","state":"testaccepted","type":"saml20-sp","revisionNote":"Created by selfregistration","allowAllEntities":false,"arpAttributes":{},"metadata":{"contacts":[{"contactType":"support","emailAddress":"DANTEITSupport@dante.net","givenName":"DANTE IT Support","surName":"","telephoneNumber":""},{"contactType":"technical","emailAddress":"it@dante.net","givenName":"DANTE IT","surName":"","telephoneNumber":""}],"NameIDFormats":["urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress","urn:oasis:names:tc:SAML:2.0:nameid-format:persistent","urn:oasis:names:tc:SAML:2.0:nameid-format:transient"],"AssertionConsumerService":[{"Binding":"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST","Location":"https://ps-ui-test.qalab.geant.net/perfsonar-ui/saml/SAMLAssertionConsumer","index":"0"},{"Binding":"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact","Location":"https://ps-ui-test.qalab.geant.net/perfsonar-ui/saml/SAMLAssertionConsumer","index":"1"}],"coin":{"gadgetbaseurl":"comsumer key","oauth":{"secret":"secret","callback_url":"http://localhost"}}},"allowedConnections":[],"blockedConnections":[],"disableConsentConnections":[],"active":false}' \
https://serviceregistry.test.surfconext.nl/janus/app.php/api/connections.json
