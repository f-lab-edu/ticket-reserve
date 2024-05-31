package com.kjh.ticketreserve.annotation;

import autoparams.AutoSource;
import autoparams.BrakeBeforeAnnotation;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@AutoSource
@BrakeBeforeAnnotation(Autowired.class)
public @interface AutoDomainSource {
}
