package basic;

import kdd08.CodeCost;

public class Test {
	public static void main(String[] args) throws Exception{
		EventManager manager = new EventManager();

		manager.read("c:/dataset/event/short.txt");
		manager.read("g:/dataset/event/application.txt");
		System.out.println("Distinct event size:" + manager.distinctEventSize());
		System.out.println("Event length:" + manager.length());
//		CodeCost codeCost = new CodeCost(manager);
//		double cost = codeCost.optimalTL();
//		System.out.println(cost);
	}
	
}
