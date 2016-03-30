package net.chrisrichardson.eventstore.examples.kanban.commonwebsocket;

/**
 * Created by popikyardo on 13.10.15.
 */
//
//SECURITY @Configuration
public class WebSocketSecurityConfig {//extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    //SECURITY
    /*
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.anyMessage().permitAll();
    }*/

    /**
     * Disables CSRF for Websockets.
     */
    //SECURITY @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
