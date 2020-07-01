package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description
 * @date 2019/5/20
 */
public class LifeRecruitmentParams extends LifeListBaseParams {
    private String city;//区域
    private String industryRequirements;//行业
    private String jobFunctions;//工作性质
    private String experienceRequirements;//经验要求

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIndustryRequirements() {
        return industryRequirements;
    }

    public void setIndustryRequirements(String industryRequirements) {
        this.industryRequirements = industryRequirements;
    }

    public String getJobFunctions() {
        return jobFunctions;
    }

    public void setJobFunctions(String jobFunctions) {
        this.jobFunctions = jobFunctions;
    }

    public String getExperienceRequirements() {
        return experienceRequirements;
    }

    public void setExperienceRequirements(String experienceRequirements) {
        this.experienceRequirements = experienceRequirements;
    }
}
