package com.project.gymcrmsystem.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class StorageInitializationPostProcessor implements BeanPostProcessor {

  @Value("${storage.data.file}")
  private Resource dataFile;
}
