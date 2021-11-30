package model.dto;

import java.sql.Date;

public class Animal extends DTO<Integer> {
    private final Integer category;
    private final String name;
    private final Date birthDate;
    private final String birthPlace;
    private final Date vaccinationDate;
    private final Date lastVisit;

    public Animal(int id, Integer category, String name, Date birthDate, String birthPlace, Date vaccinationDate, Date lastVisit) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.vaccinationDate = vaccinationDate;
        this.lastVisit = lastVisit;
    }

    public Integer getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public Date getVaccinationDate() {
        return vaccinationDate;
    }

    public Date getLastVisit() {
        return lastVisit;
    }
}
