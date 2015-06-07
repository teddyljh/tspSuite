package org.logisticPlanning.tsp.solving.algorithms.localSearch.permutation.simulatedAnnealing;


import java.io.PrintStream;

import org.logisticPlanning.tsp.benchmarking.instances.Instance;
import org.logisticPlanning.tsp.benchmarking.objective.ObjectiveFunction;
import org.logisticPlanning.tsp.solving.TSPAlgorithmRunner;
import org.logisticPlanning.tsp.solving.TSPModule;
import org.logisticPlanning.tsp.solving.TSPAlgorithm;
import org.logisticPlanning.tsp.solving.operators.permutation.creation.PermutationCreateCanonical;
import org.logisticPlanning.tsp.solving.operators.permutation.update.PermutationUpdateOperator;
import org.logisticPlanning.tsp.solving.operators.permutation.update.PermutationUpdate_Swap;
import org.logisticPlanning.utils.config.Configurable;
import org.logisticPlanning.utils.config.Configuration;
import org.logisticPlanning.utils.math.random.Randomizer;

public class SimulatedAnnealing extends org.logisticPlanning.tsp.solving.TSPAlgorithm
{
	  /** the serial version uid */
	  private static final long serialVersionUID = 1L;
	  
	  /** the parameter for the update operation: {@value} */
	  public static final String PARAM_UPDATE_OPERATION = "updateOperation";
	  
	  /** the parameter for the initial temperature: {@value} */
	  public static final String INITIAL_TEMPERATURE = "initialTemperature";
	  
	  /** the parameter for the cooling rate: {@value} */
	  public static final String COOLING_RATE = "coolingRate";
	  
	  /** the parameter for the critical temperature: {@value} */
	  public static final String CRITICAL_TEMPERATURE = "criticalTemp";
	  
	  /** the parameter for the constant probability: {@value} */
	  public static final String CONSTANT_PROBABILITY = "constantProbability";

	  /** the permutation */
	  private transient int[] m_sol;
	  
	  /** the update */
	  private PermutationUpdateOperator m_update;
	  
	  /** initial temperature*/
	  private double initialTemp;
	  
	  /** set cooling rate */
	  private double coolingRate;
	  
	 /** theoretical value of constant probability */
	  private double constProbability; 
	  
	  /** theoretical value of the critical temperature
	   * when constant probability should start */ 
	  private double criticalTemp;
	  
	  /** instantiate */
	  public SimulatedAnnealing()
	  {
		  super("Simulated Annealing");
		  this.m_update = PermutationUpdate_Swap.INSTANCE;
		  
		  //Default cooling rate
		  this.coolingRate = 0.997;
		  
		  //Default initial temp
		  this.initialTemp = 10000;
		  
		  this.constProbability = 0.07;
		  
		  this.criticalTemp = 2;
		  
	  }
	  
	  
	  /**
	   * Perform the simulated annealing algorithm
	   * 
	   * @param args
	   *          the command line arguments
	   */
	  
	  public static void main(final String[] args) 
	  {
	       TSPAlgorithmRunner.benchmark(org.logisticPlanning.tsp.benchmarking.instances.Instance.SYMMETRIC_INSTANCES,
	          SimulatedAnnealing.class,
	          args);
	   }
	  
	  @Override
	  public final void solve(final ObjectiveFunction f)
	 { 
	        // Set temp to initial temp
		     double temp = initialTemp;

   
	        final int n;
	        final Randomizer r;
	        final PermutationUpdateOperator op;
	        int[] sol;
	        long tourLength;
	        int change;
	        int pos1, pos2;
	        
	        r = f.getRandom();
	        n = f.n();
	        
	        sol = this.m_sol;
	        if ((sol == null) || (sol.length != n)) {
	          this.m_sol = sol = new int[n];
	        }

	       // initialize permutation
	        PermutationCreateCanonical.makeCanonical(sol, n);
	        r.shuffle(sol, 0, n);

	       // get the length of the initial permutation
	       tourLength = f.evaluate(sol);

	       op = this.m_update;
	       
	       //loop until system has cooled 
	       while (temp > 1 && f.shouldTerminate())
	       {
	    	   // Get two random positions in the tour
	    	   pos1 = r.nextInt(n);
	    	   do {
	    		   pos2 = r.nextInt(n);
	    	   } while (pos1 == pos2);
	    	   
	    	   //calculate the change in tour 
	    	   change = op.delta(sol, f, pos1, pos2);
	    	   
	    	   //calculate Metropolis probability
	    	   double metroProbability = acceptMetropolisProb(change, temp);
	    	   
	    	   double randomCheck = r.nextDouble();
	    	   
	    	   if (( metroProbability > randomCheck) ||
	    		  ( temp < criticalTemp && constProbability > randomCheck))
	    	   {
	    		   //accept change under METROPOLIS criterion or CONSTANT criterion 
	    		   //apply change, register FE
	    	        tourLength += change;
	    	        op.update(sol, pos1, pos2);
	    	        f.registerFE(sol, tourLength);
	    	   }
	    	   
	    	   //Cool the system
	    	   temp *= coolingRate;
	        
	       }	        	        
     }
	
	  /** Calculate the Metropolis acceptance probability */
	   public final double acceptMetropolisProb(double change, double temperature) 
	   {
	       // If the new solution is better, accept it
	       if (change < 0) {
	            return 1.0;
	       }
	        // If the new solution is worse, calculate an acceptance probability
	       return Math.exp( - change / temperature);
        
     }

	   
	   @Override
	   public SimulatedAnnealing clone()
	   {
		   final SimulatedAnnealing clo;
		   clo = ((SimulatedAnnealing)(super.clone()));
		   clo.m_sol = null;
		   clo.m_update = clo.m_update.clone();
		   
		   clo.initialTemp = this.initialTemp;
		   clo.coolingRate = this.coolingRate;
		   
		   clo.constProbability = this.constProbability ;
		   clo.criticalTemp = this.criticalTemp;
		   
		   return clo;
		   
	   }
	   
	   
	   
	   /** {@inheritDoc} */
	   @Override
	   public void beginRun(final ObjectiveFunction f) {
	     super.beginRun(f);
	     this.m_update.beginRun(f);
	     this.m_sol = new int[f.n()];

	   }

	   /** {@inheritDoc} */
	   @Override
	   public void endRun(final ObjectiveFunction f) {
	     this.m_sol = null;
	     try {
	       this.m_update.endRun(f);
	     } finally {
	       super.endRun(f);
	     }
	   }
	   
	   /** {@inheritDoc} */
	   @Override
	   public void configure(final Configuration config) {
	     super.configure(config);

	     this.m_update = config.getInstance(
	         SimulatedAnnealing.PARAM_UPDATE_OPERATION,
	         PermutationUpdateOperator.class, null, this.m_update);
	     
	     //max value for initialTemp set for args
	     this.initialTemp = config.getDouble(
	    		 SimulatedAnnealing.INITIAL_TEMPERATURE,
	    		 0 , Math.pow(10, 10), this.initialTemp);
	     
	     this.coolingRate = config.getDouble(
	    		 SimulatedAnnealing.COOLING_RATE, 
	    		 0, 1, this.coolingRate);
	     
	     this.criticalTemp = config.getDouble(
	    		 ProblemDependentSA.CRITICAL_TEMPERATURE, 
	    		 this.initialTemp/10000, this.initialTemp, 
	    		 this.criticalTemp);
	     
	     this.constProbability = config.getDouble(
	    		 ProblemDependentSA.CONSTANT_PROBABILITY, 
	    		 0, 0.1, this.constProbability);
	   }

	   /** {@inheritDoc} */
	   @Override
	   public void printConfiguration(final PrintStream ps) {
	     super.printConfiguration(ps);

	     Configurable.printKey(SimulatedAnnealing.PARAM_UPDATE_OPERATION, ps);
	     
	     ps.println(this.m_update);
	     
	     Configurable.printKey(SimulatedAnnealing.INITIAL_TEMPERATURE, ps);
	     ps.println(this.initialTemp);
	     
	     Configurable.printKey(SimulatedAnnealing.COOLING_RATE, ps);
	     ps.println(this.coolingRate);
	     
	     Configurable.printKey(ProblemDependentSA.CRITICAL_TEMPERATURE, ps);
	     ps.println(this.criticalTemp);
	     
	     Configurable.printKey(ProblemDependentSA.CONSTANT_PROBABILITY, ps);
	     ps.println(this.constProbability);
	     
	   }

	   /** {@inheritDoc} */
	   @Override
	   public void printParameters(final PrintStream ps) {

	     super.printParameters(ps);

	     Configurable.printKey(SimulatedAnnealing.PARAM_UPDATE_OPERATION, ps);
	     ps.println("the update operation to use in the annealing."); //$NON-NLS-1$
	     
	     Configurable.printKey(SimulatedAnnealing.INITIAL_TEMPERATURE, ps);
	     ps.println("The initial temperature used in the simulated annealing: ");
	     
	     Configurable.printKey(SimulatedAnnealing.COOLING_RATE, ps);
	     ps.println("The cooling rate used in the simulated annealing"
	     		+ "(e.g.cooling rate = 0.99, "
	     		+ "then every time the temperature will be by 0.99*temperature : ");
	     
	     Configurable.printKey(ProblemDependentSA.CRITICAL_TEMPERATURE, ps);
	     ps.println("The ciritical temperature that divides "
	     		+ "between Metropolis acceptance probability and"
	     		+ "constant acceptance probability :");
	     
	     Configurable.printKey(ProblemDependentSA.CONSTANT_PROBABILITY, ps);
	     ps.println("The constant acceptance probability :");
	   }

}


