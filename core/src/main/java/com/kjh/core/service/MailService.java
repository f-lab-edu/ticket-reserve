package com.kjh.core.service;

import com.kjh.core.template.TemplateCode;

import java.util.Map;

public interface MailService {

    void send(TemplateCode templateCode, String email, Map<String, Object> model);
}
