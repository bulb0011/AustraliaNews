package com.ruanyun.australianews.model.params;

/**
 * @author hdl
 * @description 修改我的发布信息
 * @date 2019/3/25
 */
public class UpdateMyReleaseInfoParams {
    private String commonOid;//	是公共编号
    private String lifeType;//	否类型 1房屋出租 2房屋求租 3二手闲置 4同城交友 5招聘信息 6宠物交易 7生意转让 8汽车买卖 9搬家清洁 10电子维修 11汽车维修 12花园杀虫 13接机服务 14装修建筑 15紧急求助
    private String images;//	否附件图（逗号隔开）
    private String mainPhoto;//	否主图（逗号隔开）
    private String title;//	否标题
    private String authenticationNum;//	否认证号
    private String companyName;//	否公司名称
    private String companyAddress;//	否公司地址
    private String companyWebsite;//	否公司网址
    private String transactionNature;//	否来源 1个人 2商家
    private String description;//	否生活详情
    private String city;//	否城市
    private String address;//	否详细地址
    private String linkMan;//	否联系人
    private String linkTel;//	否联系电话
    private String email;//	否邮箱
    private String qq;//	否qq
    private String weixin;//	否微信
    private String price;//	否
    private String rentalMethod;//	否1/2出租方式（1整租 2合租）
    private String rent;//	否1/2出租租金
    private String identity;//	否1/2房屋来源(1 个人  2中介)
    private String rentType;//	否1/2房屋类型（1住宅楼 2住宅楼）
    private String houseCharacteristics;//	否1/2房屋配置（1床 2包电 3包水 4冰箱 5空调 6包宽带 7包煤气 8电视剧 9停车位 10微波炉 11洗衣机 ）
    private String nearbyFacilities;//	否1/2附近设施（1超市 2学校 3公车站 4火车站 5健身房 6游泳池）
    private String checkInTime;//	否"2017-5-5" 1/2入住时间
    private String isPet;//	否1/2是否允许宠物（1允许 2不允许）
    private String expectedType;//	否2期望户型（1一室 2二室 3三室 4四室 5四室以上）
    private String resource;//	否3来源 1个人 2中介
    private String classification;//	否3分类
    private String age;//	否4年龄
    private String height;//	否4身高
    private String weight;//	否4体重
    private String sex;//	否4性别
    private String name;//	否4姓名
    private String educationBackground;//	否4学历
    private String position;//	否4职业
    private String detail;//	否4征友详情
    private String companyDescription;//	否5公司介绍
    private String salary;//	否5工资水平
    private String jobFunctions;//	否5工作性质
    private String industryRequirements;//	否5行业要求
    private String experienceRequirements;//	否5经验要求
    private String sexRequirements;//	否5性别要求
    private String educationalRequirements;//	否5学历要求
    private String recruiyCount;//	否5招聘人数
    private String recruiyDetail;//	否5招聘详情
    private String source;//	否5来源 1中介 2公司 3个人
    private String petDetail;//	否6宠物详情
    private String petType;//	否6宠物分类
    private String transferPrice;//	转让价格
    private String businessTransferDetail;//	否7转让详情
    private String businessTransferType;//	否7行业
    private String year;//	否8年份
    private String brand;//	否8品牌
    private String transmission;//	否8变速箱
    private String kilometer;//	否8公里数
    private String carDetail;//	否8汽车详情
    private String movingCleaningDetail;//	否9详情
    private String movingCleaningType;//	否9类型 1搬家 2清洁
    private String electronicMaintenanceDetail;//	否10详情
    private String electronicMaintenanceType;//	否10类型 1电脑 2电视 3手机 4游戏机
    private String carRepairDetail;//	否11详情
    private String carRepairType;//	否11类型 1汽车 2汽车配件
    private String gardenInsecticideDetail;//	否12详情
    private String gardenInsecticideType;//	否12类型 园艺 杀虫
    private String mannedPopulation;//	载人数量
    private String pickUpDetail;//	否13详情
    private String pickUpType;//	否13类型 1.4人轿车 2.7人商务车
    private String decorationBuildDetail;//	否14详情
    private String decorationBuildType;//	否14类型 （分类 1地板 2隔间 3办公室 4商铺门店）
    private String emergencyHelpDetail;//	否15详情
    private String emergencyHelpType;//	否15类型（分类 1紧急求助 2上门开锁）

    public String getCommonOid() {
        return commonOid;
    }

    public void setCommonOid(String commonOid) {
        this.commonOid = commonOid;
    }

    public String getLifeType() {
        return lifeType;
    }

    public void setLifeType(String lifeType) {
        this.lifeType = lifeType;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthenticationNum() {
        return authenticationNum;
    }

    public void setAuthenticationNum(String authenticationNum) {
        this.authenticationNum = authenticationNum;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getTransactionNature() {
        return transactionNature;
    }

    public void setTransactionNature(String transactionNature) {
        this.transactionNature = transactionNature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMannedPopulation() {
        return mannedPopulation;
    }

    public void setMannedPopulation(String mannedPopulation) {
        this.mannedPopulation = mannedPopulation;
    }

    public String getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(String transferPrice) {
        this.transferPrice = transferPrice;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRentalMethod() {
        return rentalMethod;
    }

    public void setRentalMethod(String rentalMethod) {
        this.rentalMethod = rentalMethod;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public String getHouseCharacteristics() {
        return houseCharacteristics;
    }

    public void setHouseCharacteristics(String houseCharacteristics) {
        this.houseCharacteristics = houseCharacteristics;
    }

    public String getNearbyFacilities() {
        return nearbyFacilities;
    }

    public void setNearbyFacilities(String nearbyFacilities) {
        this.nearbyFacilities = nearbyFacilities;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getIsPet() {
        return isPet;
    }

    public void setIsPet(String isPet) {
        this.isPet = isPet;
    }

    public String getExpectedType() {
        return expectedType;
    }

    public void setExpectedType(String expectedType) {
        this.expectedType = expectedType;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducationBackground() {
        return educationBackground;
    }

    public void setEducationBackground(String educationBackground) {
        this.educationBackground = educationBackground;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getJobFunctions() {
        return jobFunctions;
    }

    public void setJobFunctions(String jobFunctions) {
        this.jobFunctions = jobFunctions;
    }

    public String getIndustryRequirements() {
        return industryRequirements;
    }

    public void setIndustryRequirements(String industryRequirements) {
        this.industryRequirements = industryRequirements;
    }

    public String getExperienceRequirements() {
        return experienceRequirements;
    }

    public void setExperienceRequirements(String experienceRequirements) {
        this.experienceRequirements = experienceRequirements;
    }

    public String getSexRequirements() {
        return sexRequirements;
    }

    public void setSexRequirements(String sexRequirements) {
        this.sexRequirements = sexRequirements;
    }

    public String getEducationalRequirements() {
        return educationalRequirements;
    }

    public void setEducationalRequirements(String educationalRequirements) {
        this.educationalRequirements = educationalRequirements;
    }

    public String getRecruiyCount() {
        return recruiyCount;
    }

    public void setRecruiyCount(String recruiyCount) {
        this.recruiyCount = recruiyCount;
    }

    public String getRecruiyDetail() {
        return recruiyDetail;
    }

    public void setRecruiyDetail(String recruiyDetail) {
        this.recruiyDetail = recruiyDetail;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPetDetail() {
        return petDetail;
    }

    public void setPetDetail(String petDetail) {
        this.petDetail = petDetail;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getBusinessTransferDetail() {
        return businessTransferDetail;
    }

    public void setBusinessTransferDetail(String businessTransferDetail) {
        this.businessTransferDetail = businessTransferDetail;
    }

    public String getBusinessTransferType() {
        return businessTransferType;
    }

    public void setBusinessTransferType(String businessTransferType) {
        this.businessTransferType = businessTransferType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getKilometer() {
        return kilometer;
    }

    public void setKilometer(String kilometer) {
        this.kilometer = kilometer;
    }

    public String getCarDetail() {
        return carDetail;
    }

    public void setCarDetail(String carDetail) {
        this.carDetail = carDetail;
    }

    public String getMovingCleaningDetail() {
        return movingCleaningDetail;
    }

    public void setMovingCleaningDetail(String movingCleaningDetail) {
        this.movingCleaningDetail = movingCleaningDetail;
    }

    public String getMovingCleaningType() {
        return movingCleaningType;
    }

    public void setMovingCleaningType(String movingCleaningType) {
        this.movingCleaningType = movingCleaningType;
    }

    public String getElectronicMaintenanceDetail() {
        return electronicMaintenanceDetail;
    }

    public void setElectronicMaintenanceDetail(String electronicMaintenanceDetail) {
        this.electronicMaintenanceDetail = electronicMaintenanceDetail;
    }

    public String getElectronicMaintenanceType() {
        return electronicMaintenanceType;
    }

    public void setElectronicMaintenanceType(String electronicMaintenanceType) {
        this.electronicMaintenanceType = electronicMaintenanceType;
    }

    public String getCarRepairDetail() {
        return carRepairDetail;
    }

    public void setCarRepairDetail(String carRepairDetail) {
        this.carRepairDetail = carRepairDetail;
    }

    public String getCarRepairType() {
        return carRepairType;
    }

    public void setCarRepairType(String carRepairType) {
        this.carRepairType = carRepairType;
    }

    public String getGardenInsecticideDetail() {
        return gardenInsecticideDetail;
    }

    public void setGardenInsecticideDetail(String gardenInsecticideDetail) {
        this.gardenInsecticideDetail = gardenInsecticideDetail;
    }

    public String getGardenInsecticideType() {
        return gardenInsecticideType;
    }

    public void setGardenInsecticideType(String gardenInsecticideType) {
        this.gardenInsecticideType = gardenInsecticideType;
    }

    public String getPickUpDetail() {
        return pickUpDetail;
    }

    public void setPickUpDetail(String pickUpDetail) {
        this.pickUpDetail = pickUpDetail;
    }

    public String getPickUpType() {
        return pickUpType;
    }

    public void setPickUpType(String pickUpType) {
        this.pickUpType = pickUpType;
    }

    public String getDecorationBuildDetail() {
        return decorationBuildDetail;
    }

    public void setDecorationBuildDetail(String decorationBuildDetail) {
        this.decorationBuildDetail = decorationBuildDetail;
    }

    public String getDecorationBuildType() {
        return decorationBuildType;
    }

    public void setDecorationBuildType(String decorationBuildType) {
        this.decorationBuildType = decorationBuildType;
    }

    public String getEmergencyHelpDetail() {
        return emergencyHelpDetail;
    }

    public void setEmergencyHelpDetail(String emergencyHelpDetail) {
        this.emergencyHelpDetail = emergencyHelpDetail;
    }

    public String getEmergencyHelpType() {
        return emergencyHelpType;
    }

    public void setEmergencyHelpType(String emergencyHelpType) {
        this.emergencyHelpType = emergencyHelpType;
    }
}
