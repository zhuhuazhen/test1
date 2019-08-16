package com.webService;


public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub 
		QueryMapEntity param=new QueryMapEntity();
		param.setEquipmentId("3G*1452002156*");
		param.setYear("20150714");
		param.setTime("162323");
		param.setLatitudeIdentity("N");
		param.setLongitudeIdentity("E");
		param.setLongitude("112.9361649");//28.146232  112.948893
		param.setLatitude("28.144466");
		param.setDataStr("[3G*1452002156*00BA*UD,140815,053542,V,28.144466,N,112.9361649,E,0.00,0.0,0.0,1,100,53,0,13,00000008,6,0,460,0,29521,17161,146,29521,51323,166,29521,51321,163,29521,51322,157,29521,17163,150,29521,17162,148]");
		String str=new QueryMapServerImplService().getQueryMapServerImplPort().sendTest(param);
//		new QueryMapServerImplService().getQueryMapServerImplPort().sendAlert("1451637827", "00010008");
		System.out.println(str);
		
	}

}
