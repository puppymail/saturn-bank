package com.saturn_bank.operator.service.course;

import com.saturn_bank.operator.dto.CourseDto;
import com.saturn_bank.operator.dao.Course;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CourseClient {

    public static String DATE_STRING = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
    public static String URL = "https://cbr.ru/scripts/XML_daily.asp?date_req=" + DATE_STRING + ".xml";

    final RestTemplate restTemplate = new RestTemplate();


    public List<Course> getCourses() {
        CourseDto response = restTemplate.getForObject(URL, CourseDto.class);

        if (response != null) {
            response
                    .getValute()
                    .forEach(x -> x.setValue(BigDecimal.valueOf(Double.parseDouble(x.get_Value().replace(",", ".")))));

            return response.getValute();
        }

        return null;
    }
}
