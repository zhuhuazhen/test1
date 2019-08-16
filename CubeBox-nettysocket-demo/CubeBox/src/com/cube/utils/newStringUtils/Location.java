package com.cube.utils.newStringUtils;

/**
 * @ClassName: Location
 * @Description: 位置数据
 * @author Tomlin
 * @date 2015-6-9 上午11:50:34
 * @version V1.0
 */
public class Location {
	
	/**
	 * 日期
	 */
	private String year;
	
	/**
	 * 时间
	 */
	private String time;
	
	/**
	 * 是否定位<br>
	 * A:定位；V:未定位
	 */
	private String isLocation;
	
	/**
	 * 纬度
	 */
	private String latitude;
	
	/**
	 * 纬度标识<br>
	 * N:北纬； S：南纬
	 */
	private String latitudeIdentity;
	
	/**
	 * 经度
	 */
	private String longitude;
	
	/**
	 * 经度标识<br>
	 * E:东经； W:西经
	 */
	private String longitudeIdentity;
	
	/**
	 * 速度<br>
	 * 单位：公里/小时
	 */
	private String speed;
	
	/**
	 * 方向<br>
	 * 度数
	 */
	private String direction;
	
	/**
	 * 海拔<br>
	 * 单位：米
	 */
	private String altitude;
	
	/**
	 * 卫星个数
	 */
	private String satelliteNumber;
	
	/**
	 * 信号强度<br>
	 * 范围：0-100
	 */
	private String signalStrength;
	
	/**
	 * 电量百分比
	 */
	private String powerPercent;
	
	/**
	 * 计步数
	 */
	private String stepNumber;
	
	/**
	 * 翻滚次数
	 */
	private String rollNumber;
	
	/**
	 * 终端状态<br>
	 * 用十六进制字符串表示,含义如下:
		高 16bit 表示报警,低 16bit 表示状态.
		Bit 位(0 开始)       含义(1 有效)
		0                    低电状态
		1                    出围栏状态
		2                    进围栏状态
		3                    手环状态
		16                   SOS 报警
		17                   低电报警
		18                   出围栏报警
		19                   进围栏报警
		20                   手环拆除报警
	 */
	private String status;
	
	/**
	 * 基站个数
	 */
	private String stationNumber;
	
	/**
	 * 连接基站延时
	 */
	private String delay;
	
	/**
	 * MCC国家码
	 */
	private String countryCode;
	
	/**
	 * 网号<br>
	 * 中国移动： 00， 02， 04， 06<br>
	 * 中国电信： 03<br>
	 * 中国联通： 01，05，07
	 */
	private String networkCode;
	
	/**
	 * 连接基站位置区域码
	 */
	private String connectStationAreaCode;
	
	/**
	 * 连接基站编号
	 */
	private String connectStationNumber;
	
	/**
	 * 连接基站信号强度
	 */
	private String connectStationSignalStrength;
	
	/**
	 * 附近基站1位置区域码
	 */
	private String nearbyStationAreaCode1;
	
	/**
	 * 附近基站1编号
	 */
	private String nearbyStationNumber1;
	
	/**
	 * 附近基站1信号强度
	 */
	private String nearbyStationSignalStrength1;
	
	/**
	 * 附近基站2位置区域码
	 */
	private String nearbyStationAreaCode2;
	
	/**
	 * 附近基站2编号
	 */
	private String nearbyStationNumber2;
	
	/**
	 * 附近基站3信号强度
	 */
	private String nearbyStationSignalStrength2;
	
	/**
	 * 附近基站3位置区域码
	 */
	private String nearbyStationAreaCode3;
	
	/**
	 * 附近基站3编号
	 */
	private String nearbyStationNumber3;
	
	/**
	 * 附近基站3信号强度
	 */
	private String nearbyStationSignalStrength3;

	/**
	 * 附近基站4位置区域码
	 */
	private String nearbyStationAreaCode4;
	
	/**
	 * 附近基站4编号
	 */
	private String nearbyStationNumber4;
	
	/**
	 * 附近基站4信号强度
	 */
	private String nearbyStationSignalStrength4;
	
	/**
	 * 附近基站5位置区域码
	 */
	private String nearbyStationAreaCode5;
	
	/**
	 * 附近基站5编号
	 */
	private String nearbyStationNumber5;
	
	/**
	 * 附近基站5信号强度
	 */
	private String nearbyStationSignalStrength5;
	
	/**
	 * 附近基站6位置区域码
	 */
	private String nearbyStationAreaCode6;
	
	/**
	 * 附近基站6编号
	 */
	private String nearbyStationNumber6;
	
	/**
	 * 附近基站6信号强度
	 */
	private String nearbyStationSignalStrength6;
	/**
	 * 操作类型
	 */
	private String opType;
	
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIsLocation() {
		return isLocation;
	}

	public void setIsLocation(String isLocation) {
		this.isLocation = isLocation;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLatitudeIdentity() {
		return latitudeIdentity;
	}

	public void setLatitudeIdentity(String latitudeIdentity) {
		this.latitudeIdentity = latitudeIdentity;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLongitudeIdentity() {
		return longitudeIdentity;
	}

	public void setLongitudeIdentity(String longitudeIdentity) {
		this.longitudeIdentity = longitudeIdentity;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getSatelliteNumber() {
		return satelliteNumber;
	}

	public void setSatelliteNumber(String satelliteNumber) {
		this.satelliteNumber = satelliteNumber;
	}

	public String getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(String signalStrength) {
		this.signalStrength = signalStrength;
	}

	public String getPowerPercent() {
		return powerPercent;
	}

	public void setPowerPercent(String powerPercent) {
		this.powerPercent = powerPercent;
	}

	public String getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(String stepNumber) {
		this.stepNumber = stepNumber;
	}

	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStationNumber() {
		return stationNumber;
	}

	public void setStationNumber(String stationNumber) {
		this.stationNumber = stationNumber;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getNetworkCode() {
		return networkCode;
	}

	public void setNetworkCode(String networkCode) {
		this.networkCode = networkCode;
	}

	public String getConnectStationAreaCode() {
		return connectStationAreaCode;
	}

	public void setConnectStationAreaCode(String connectStationAreaCode) {
		this.connectStationAreaCode = connectStationAreaCode;
	}

	public String getConnectStationNumber() {
		return connectStationNumber;
	}

	public void setConnectStationNumber(String connectStationNumber) {
		this.connectStationNumber = connectStationNumber;
	}

	public String getConnectStationSignalStrength() {
		return connectStationSignalStrength;
	}

	public void setConnectStationSignalStrength(String connectStationSignalStrength) {
		this.connectStationSignalStrength = connectStationSignalStrength;
	}

	public String getNearbyStationAreaCode1() {
		return nearbyStationAreaCode1;
	}

	public void setNearbyStationAreaCode1(String nearbyStationAreaCode1) {
		this.nearbyStationAreaCode1 = nearbyStationAreaCode1;
	}

	public String getNearbyStationNumber1() {
		return nearbyStationNumber1;
	}

	public void setNearbyStationNumber1(String nearbyStationNumber1) {
		this.nearbyStationNumber1 = nearbyStationNumber1;
	}

	public String getNearbyStationSignalStrength1() {
		return nearbyStationSignalStrength1;
	}

	public void setNearbyStationSignalStrength1(String nearbyStationSignalStrength1) {
		this.nearbyStationSignalStrength1 = nearbyStationSignalStrength1;
	}

	public String getNearbyStationAreaCode2() {
		return nearbyStationAreaCode2;
	}

	public void setNearbyStationAreaCode2(String nearbyStationAreaCode2) {
		this.nearbyStationAreaCode2 = nearbyStationAreaCode2;
	}

	public String getNearbyStationNumber2() {
		return nearbyStationNumber2;
	}

	public void setNearbyStationNumber2(String nearbyStationNumber2) {
		this.nearbyStationNumber2 = nearbyStationNumber2;
	}

	public String getNearbyStationSignalStrength2() {
		return nearbyStationSignalStrength2;
	}

	public void setNearbyStationSignalStrength2(String nearbyStationSignalStrength2) {
		this.nearbyStationSignalStrength2 = nearbyStationSignalStrength2;
	}

	public String getNearbyStationAreaCode3() {
		return nearbyStationAreaCode3;
	}

	public void setNearbyStationAreaCode3(String nearbyStationAreaCode3) {
		this.nearbyStationAreaCode3 = nearbyStationAreaCode3;
	}

	public String getNearbyStationNumber3() {
		return nearbyStationNumber3;
	}

	public void setNearbyStationNumber3(String nearbyStationNumber3) {
		this.nearbyStationNumber3 = nearbyStationNumber3;
	}

	public String getNearbyStationSignalStrength3() {
		return nearbyStationSignalStrength3;
	}

	public void setNearbyStationSignalStrength3(String nearbyStationSignalStrength3) {
		this.nearbyStationSignalStrength3 = nearbyStationSignalStrength3;
	}

	public String getNearbyStationAreaCode4() {
		return nearbyStationAreaCode4;
	}

	public void setNearbyStationAreaCode4(String nearbyStationAreaCode4) {
		this.nearbyStationAreaCode4 = nearbyStationAreaCode4;
	}

	public String getNearbyStationNumber4() {
		return nearbyStationNumber4;
	}

	public void setNearbyStationNumber4(String nearbyStationNumber4) {
		this.nearbyStationNumber4 = nearbyStationNumber4;
	}

	public String getNearbyStationSignalStrength4() {
		return nearbyStationSignalStrength4;
	}

	public void setNearbyStationSignalStrength4(String nearbyStationSignalStrength4) {
		this.nearbyStationSignalStrength4 = nearbyStationSignalStrength4;
	}

	public String getNearbyStationAreaCode5() {
		return nearbyStationAreaCode5;
	}

	public void setNearbyStationAreaCode5(String nearbyStationAreaCode5) {
		this.nearbyStationAreaCode5 = nearbyStationAreaCode5;
	}

	public String getNearbyStationNumber5() {
		return nearbyStationNumber5;
	}

	public void setNearbyStationNumber5(String nearbyStationNumber5) {
		this.nearbyStationNumber5 = nearbyStationNumber5;
	}

	public String getNearbyStationSignalStrength5() {
		return nearbyStationSignalStrength5;
	}

	public void setNearbyStationSignalStrength5(String nearbyStationSignalStrength5) {
		this.nearbyStationSignalStrength5 = nearbyStationSignalStrength5;
	}

	public String getNearbyStationAreaCode6() {
		return nearbyStationAreaCode6;
	}

	public void setNearbyStationAreaCode6(String nearbyStationAreaCode6) {
		this.nearbyStationAreaCode6 = nearbyStationAreaCode6;
	}

	public String getNearbyStationNumber6() {
		return nearbyStationNumber6;
	}

	public void setNearbyStationNumber6(String nearbyStationNumber6) {
		this.nearbyStationNumber6 = nearbyStationNumber6;
	}

	public String getNearbyStationSignalStrength6() {
		return nearbyStationSignalStrength6;
	}

	public void setNearbyStationSignalStrength6(String nearbyStationSignalStrength6) {
		this.nearbyStationSignalStrength6 = nearbyStationSignalStrength6;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}
	
}
