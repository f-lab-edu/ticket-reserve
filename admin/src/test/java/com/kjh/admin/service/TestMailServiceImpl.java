package com.kjh.admin.service;

import com.kjh.core.service.MailService;
import com.kjh.core.template.TemplateCode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

@Profile("test")
@Service
public class TestMailServiceImpl implements MailService {

    @Override
    public void sendAsync(TemplateCode templateCode, String email, Map<String, Object> model) {
    }
}
