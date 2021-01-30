import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.*; 


public class minMaxTeamFormation {

	public static double minimax(int depth, int maxDepth, boolean playerA, double [][] chefAttributes, double maxAdvantage, double [] firstGuyPicked) {
		// base case
		if (depth == 0) {
			double advantage = computeTotalPower(chefAttributes);
			return advantage;
		}

		if (playerA) {
			double maxEval = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < chefAttributes.length; ++i)
			{
				if (chefAttributes[i][4] == 0)
				{
					chefAttributes[i][4] = 1;

					double eval = minimax(depth-1, maxDepth, false, chefAttributes, maxAdvantage, firstGuyPicked);
					chefAttributes[i][4] = 0;
					
					//Handle tiebreaker
					if (eval != maxEval)
					{
						if (eval > maxEval && depth == maxDepth)
						{
							firstGuyPicked[0] = chefAttributes[i][0];
						}
						maxEval = Math.max(maxEval, eval);
					}
				}
			}
			return maxEval;
		}
		else
		{
			double minEval = Double.POSITIVE_INFINITY; 
			for (int i = 0; i < chefAttributes.length; ++i)
			{
				if (chefAttributes[i][4] == 0)
				{
					chefAttributes[i][4] = 2;
					double eval = minimax(depth-1, maxDepth, true, chefAttributes, maxAdvantage, firstGuyPicked);
					chefAttributes[i][4] = 0;
					minEval = Math.min(minEval, eval);
				}
			}
			return minEval;
		}
	}
	public static double alphabeta(int depth, int maxDepth, boolean playerA, double [][] chefAttributes, double maxAdvantage, double [] firstGuyPicked, double alpha, double beta) {
		// base case
		if (depth == 0) {
			double advantage = computeTotalPower(chefAttributes);
			return advantage;
		}

		if (playerA) {
			double maxEval = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < chefAttributes.length; ++i)
			{
				if (chefAttributes[i][4] == 0)
				{
					chefAttributes[i][4] = 1;
					double eval = alphabeta(depth-1, maxDepth, false, chefAttributes, maxAdvantage, firstGuyPicked, alpha, beta);
					chefAttributes[i][4] = 0;
					
					//Handle tiebreaker
					if (eval != maxEval)
					{			
						if (eval > maxEval && depth == maxDepth)
						{
							firstGuyPicked[0] = chefAttributes[i][0];
						}
						maxEval = Math.max(maxEval, eval);
						alpha = Math.max(alpha, eval);
						if (beta <= alpha)
							break;
					}
				}
			}
			return maxEval;
		}
		else
		{
			double minEval = Double.POSITIVE_INFINITY; 
			for (int i = 0; i < chefAttributes.length; ++i)
			{
				if (chefAttributes[i][4] == 0)
				{
					chefAttributes[i][4] = 2;
					double eval = alphabeta(depth-1, maxDepth, true, chefAttributes, maxAdvantage, firstGuyPicked, alpha, beta);
					chefAttributes[i][4] = 0;
					minEval = Math.min(minEval, eval);
					beta = Math.min(beta, eval);
					if (beta <= alpha)
						break;
				}
			}
			return minEval;
		}
	}
	private static double computeTotalPower(double [][] chefAttributes)
	{
		double teamPowerA = 0.0;
		double teamPowerB = 0.0;
	
		Set<Double> teamASetLastDigit = new HashSet<>();
		Set<Double> teamBSetLastDigit = new HashSet<>();

		
		for (int i = 0; i < chefAttributes.length; ++i)
		{
			if (chefAttributes[i][4] == 1.0)
			{
				teamPowerA += chefAttributes[i][1] * chefAttributes[i][2];
				teamASetLastDigit.add(chefAttributes[i][0]%10);	
			}
			else if (chefAttributes[i][4] == 2.0)
			{
				teamPowerB += chefAttributes[i][1] * chefAttributes[i][3];
				teamBSetLastDigit.add(chefAttributes[i][0]%10);
				
			}
		}
		
		if (teamASetLastDigit.size() == 5)
		{
			teamPowerA += 120.0;
		}
		if (teamBSetLastDigit.size() == 5)
		{
			teamPowerB += 120.0;
		}
		
		return teamPowerA - teamPowerB;
	}
	
	
	public static double [][] sort2DArray(double [][] chefAttributes)
	{
		Arrays.sort(chefAttributes, new Comparator<double[]>()
		{
			@Override
			public int compare(final double[] entry1, final double [] entry2) {
				if (entry1[0] > entry2[0]) 
	                return 1; 
	            else
	                return -1;
			}
			
		});
		return chefAttributes;
	}
	
	// Find total preselected
	public static int findTotalPreselected(double [][] chefAttributes)
	{
		int totalUnselected = 0;
		
		for (int i = 0; i < chefAttributes.length; ++i)
		{
			if (chefAttributes[i][4] == 0)
			{
				totalUnselected++;
			}
		}
		
		return chefAttributes.length - totalUnselected;
	}

	public static void main(String[] args) throws Exception {
		//Read in input
		File file = new File("input6.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));

		int numChefs = Integer.parseInt(br.readLine());
		String algorithmType = br.readLine();

		double[][] chefAttributes = new double[numChefs][5];

		String customers;
		int incrementer = 0;
		

		while ((customers = br.readLine()) != null) {
			String[] chefAts = customers.split(",");

			for (int i = 0; i < chefAts.length; ++i) {
				chefAttributes[incrementer][i] = Double.parseDouble(chefAts[i]);
			}
			incrementer++;

		}
		
		
		double maxAdvantage = Double.NEGATIVE_INFINITY;
		double [] firstGuyPicked = {0.0};
		
		//Sort chefs in ascending ID's
		chefAttributes = sort2DArray(chefAttributes);

		
		int totalPreselected = findTotalPreselected(chefAttributes);
		
		//Find which team starts first
		double maxAdvantageID = 0.0;
		int maxDepth = 10-totalPreselected;
		
		//call minimax function
		if (algorithmType.equalsIgnoreCase("minimax"))
		{
			maxAdvantage = minimax(maxDepth, maxDepth, true, chefAttributes, maxAdvantage, firstGuyPicked);	
		}
		//call alpha-beta pruning function
		else if (algorithmType.equalsIgnoreCase("ab"))
		{
			maxAdvantage = alphabeta(maxDepth, maxDepth, true, chefAttributes, maxAdvantage, firstGuyPicked, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);	
		}
		
		
		try {
		      FileWriter fw = new FileWriter("output.txt");
		      fw.write(String.valueOf((int)firstGuyPicked[0]));
		      fw.close();
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		
	}
	
	
	private static void printChefAttributes2DArray(double[][] chefAttributes) {

		for (int i = 0; i < chefAttributes.length; ++i) {
			for (int j = 0; j < 5; ++j) {
				System.out.print(chefAttributes[i][j] + " ");
			}
			System.out.println();
		}
	}
}
