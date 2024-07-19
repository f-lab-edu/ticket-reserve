package com.kjh.notifier.service;

import com.kjh.core.dto.MailMessage;

public interface MailService {

    void sendAsync(MailMessage mailMessage);
}
