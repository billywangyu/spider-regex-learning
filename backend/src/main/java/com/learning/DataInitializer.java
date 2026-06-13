package com.learning;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class DataInitializer implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("✅ 数据库表结构已由 JPA 自动创建，符合设计规范。");
        System.out.println("📌 请通过 POST /api/import-data 接口上传 JSON 文件导入数据。");
    }
}
