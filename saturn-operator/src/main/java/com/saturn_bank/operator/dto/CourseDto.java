package com.saturn_bank.operator.dto;

import com.saturn_bank.operator.dao.Course;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@ToString
public class CourseDto implements Serializable {

    @XmlElement(name = "Valute")
    private List<Course> valute;

    @XmlElement(name = "Date")
    private LocalDate date;

}
