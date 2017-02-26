package com.careem.events;

import com.careem.commons.DBContextProvider;
import com.careem.domain.Position;
import com.careem.domain.Resource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceLocationListener {
    public static final String RESOURCE_LOCATION_CHANGED = "resource.location.changed";
    @Autowired
    DBContextProvider contextProvider;
    @RabbitListener(containerFactory = "rabbitListenerContainerFactory",
            bindings = {@QueueBinding(
                    value = @Queue(value = "resource_location_queue", durable = "true", autoDelete = "false"),
                    exchange = @Exchange(value = "careem_ecommerce_exchange", durable = "true"),
                    key = "location"
            )
            }
    )
    public void onMessage(Message message) {
        String payload = asString(message.getBody());

            contextProvider.withDBConnection(() -> {
                try {
                    JsonNode event = new ObjectMapper().readTree(message.getBody());
                    handleMessage(event);
                } catch (Exception e) {
                    //Implemetn retry here
                    e.printStackTrace();
                }
            });
    }

    public void handleMessage(JsonNode eventDetails) throws JsonProcessingException {
        String eventType = eventDetails.get("event").asText();
        Long resourceId = eventDetails.get("resourceId").asLong();
        switch (eventType) {
            case RESOURCE_LOCATION_CHANGED:
                Position position = new Position(eventDetails.get("latitude").asText(), eventDetails.get("longitude").asText());
                Resource.updatePosition(resourceId, position);
                break;
        }
    }

    @SneakyThrows
    private String asString(byte[] bytes) {
        return new String(bytes, "ISO-8859-1");
    }
}
