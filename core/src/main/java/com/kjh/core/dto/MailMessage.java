package com.kjh.core.dto;

import com.kjh.core.template.TemplateCode;

import java.util.Map;

public record MailMessage(TemplateCode templateCode, String email, Map<String, Object> model) {
}
