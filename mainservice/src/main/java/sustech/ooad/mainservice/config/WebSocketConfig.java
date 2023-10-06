package sustech.ooad.mainservice.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import sustech.ooad.mainservice.util.ws.ChattingRoom;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    final ChattingRoom webSocketHandler;

    public WebSocketConfig(ChattingRoom webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChattingRoom(), "/chat").setAllowedOrigins("*");
    }
}
