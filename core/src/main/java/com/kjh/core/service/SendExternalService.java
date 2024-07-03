package com.kjh.core.service;

import com.kjh.core.dto.MailMessage;
import com.kjh.core.model.*;
import com.kjh.core.template.TemplateCode;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendExternalService {

    private final MessageQueueService messageQueueService;

    public SendExternalService(MessageQueueService messageQueueService) {
        this.messageQueueService = messageQueueService;
    }

    public void sendReservationConfirmed(Reservation reservation,
                                         Showtime showtime,
                                         Seat seat,
                                         Theater theater,
                                         User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("reservationNo", reservation.getId());
        map.put("date", showtime.getShowDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        map.put("time", showtime.getShowDatetime().format(DateTimeFormatter.ofPattern("HH:mm")));
        map.put("seat", seat.getRowCode().name() + seat.getNumber());
        map.put("theater", theater.getName());
        map.put("price", reservation.getPrice());
        messageQueueService.send(Queue.MAIL, new MailMessage(TemplateCode.RESERVATION_CONFIRMED, user.getEmail(), map));
    }
}
