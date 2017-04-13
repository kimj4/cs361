package ie.errity.pd.genetic;

import ie.errity.pd.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.BitSet;
import java.util.Random;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Arrays;
import java.util.Collections;


/**
 * Provides a means of evolving a population of
 *{@link  ie.errity.pd.Prisoner Prisoners}
 *via a genetic algorithm
 * @author	Andrew Errity 99086921
 * @author      Sherri Goings (modified 4/4/2017)
 */
public class Breeder extends JPanel
{
    private Prisoner curPopulation[];
    private double mutateP, crossP; //mutate, cross probabilities
    private int selection, selParam, seed; // selection method, any associated selection parameter, random number seed
    private int popSize;
    private Random rand;

    /**
     *Create a new Genetic Breeder
     */
    public Breeder()
    {
      	//defaults
      	mutateP = .001;
      	crossP = .95;
      	selection = 0;
      	selParam = 1;
      	seed = -1;
      	rand = new Random(); //uses current time as seed
    }

    /**
     *Create a new Genetic Breeder using {@link  ie.errity.pd.Rules Rules} given
     *@param r1 parameters for the breeder
     */
    public Breeder(Rules r1)
    {
      	//init based on rules
      	mutateP = r1.getMutateP();
      	crossP = r1.getCrossP();
      	selection = r1.getSelection();
      	selParam = r1.getSelectionParameter();
      	seed = r1.getSeed();
      	if (seed==-1)
      	    rand = new Random();
      	else
      	    rand = new Random(seed); //uses seed set in rules for repeatability
    }

    public Prisoner[] fitnessProportionalSelection() {

        return this.stochasticUniversal(this.getScaledFitness());

    }

    public double[] getScaledFitness() {

        double totalFitness = 0;
        double meanFitness;
        double stdDev = 0;
        double scaledFitnessList[] = new double[popSize];

        for (int i = 0; i < popSize; i ++) {
            totalFitness += curPopulation[i].getScore();
        }

        meanFitness = totalFitness / popSize;

        for (int i = 0; i < popSize; i ++) {
            stdDev += Math.pow((curPopulation[i].getScore()-meanFitness),2);
        }

        stdDev = Math.pow(stdDev/popSize,.5);

        for (int i = 0; i < popSize; i ++) {
            if (stdDev == 0){
                scaledFitnessList[i]=1;
            }
            else{
                scaledFitnessList[i] = 1+(curPopulation[i].getScore()-meanFitness)/(2*stdDev);
            }

        }

        return scaledFitnessList;
    }

    public Prisoner[] stochasticUniversal(double[] scaledFitnessList) {
        if (selParam > popSize){
            selParam = popSize;
        }


         double runningSums[] = new double[popSize];
         double ticks[] = new double[popSize-selParam];
         double randOffset;
         Prisoner Selected[] = new Prisoner[popSize];
         ArrayList<Prisoner> subPop = new ArrayList<Prisoner>(Arrays.asList(curPopulation));
         Prisoner clonedPrisoner;

         for (int i = 0; i < popSize; i ++) {
            if (i==0){
                runningSums[i]=scaledFitnessList[i];
            }
            else {
                runningSums[i]=scaledFitnessList[i]+runningSums[i-1];
            }
        }

        for (int i = 0; i < ticks.length; i ++) {
            ticks[i]=i*runningSums[popSize-1]/popSize;
        }

        randOffset = rand.nextDouble()*runningSums[popSize-1];

        for (int i = 0; i < ticks.length; i ++) {
            ticks[i]=(ticks[i]+randOffset);
            if (ticks[i]>runningSums[popSize-1]){
                ticks[i] = ticks[i]-runningSums[popSize-1];
            }
        }

        for (int tick = 0; tick < ticks.length; tick ++) { // Loop through the ticks
            Selected[tick]=curPopulation[0]; //default
            for (int sum = popSize - 1; sum >= 0; sum --) { // Loop through scaledFitnessList
                if (ticks[tick] > runningSums[sum]){
                    Selected[tick]=curPopulation[sum+1];
                    break;
                }
            }
        }


        for (int clone = 1; clone <= selParam; clone ++){
            clonedPrisoner=bestPrisoner(subPop);
            Selected[popSize-clone]=clonedPrisoner;
            subPop.remove(clonedPrisoner);
        }


        return Selected;

    }

    public Prisoner bestPrisoner(ArrayList<Prisoner> prisonerList) {

        int bestFitness = 0;
        int bestIndex = 0;

        for (int i=0; i<prisonerList.size(); i++){
            if (prisonerList.get(i).getScore()>bestFitness){
                bestFitness=prisonerList.get(i).getScore();
                bestIndex=i;
            }
        }
        return prisonerList.get(bestIndex);
    }

    public Prisoner[] tournamentSelection() {
        // for each tournament, choose selParam number of unique individuals
        //  select one with the highest fitness
        // perform popSize number of tournaments
        Prisoner Selected[] = new Prisoner[popSize];
        int bestIndex = 0;
        double bestFitness = 0;
        ArrayList<Prisoner> curPopList = new ArrayList<Prisoner>(Arrays.asList(curPopulation));
        for (int i = 0; i < popSize; i++) {
            Collections.shuffle(curPopList);
            bestIndex = 0;
            bestFitness = 0;
            for (int j = 0; j < selParam; j++) {
                if (curPopList.get(j).getScore() > bestFitness) {
                    bestFitness = curPopList.get(j).getScore();
                    bestIndex = j;
                }
            }
            Selected[i] = curPopList.get(bestIndex);
        }

        return Selected;
    }

    /**
     *Breeds the next generation (panmictic mating) of an array of
     *{@link  ie.errity.pd.Prisoner Prisoners}
     *@param c	initial population (raw fitness of population must be calcualted previously)
     *@return the next generation
     */
    public Prisoner[] Breed(Prisoner[] c)
    {
      	curPopulation = c;	//population to breed
      	popSize = curPopulation.length;
      	Prisoner Selected[] = new Prisoner[popSize]; // parent pop after selection


      	// Select parents for next gen
      	// ***ADD CODE (make separate functions) to perform each of the types of selection***
      	// selection is an int that determines the method to use
      	// selParam is an int that can be used as a parameter for a selection method if required

      	// One silly selection method which uses rand and the selParam is given below
      	// Be sure to use "rand" class variable here as your random number generator (i.e. don't create a new one!)

      	// selection method 0, use parameter as a threshhold percentage.  If a random number is less than param,
      	// select the best individual from the population.  Otherwise select a random individual.
      	// In this method a param of 0 gives random selection, and a param of 100 gives best-wins-all selection.
      	if (selection == 0) {
      	    // find index of most fit individual
      	    double maxFit = 0;
      	    int indexBest = 0;
      	    for (int i=0; i<popSize; i++) {
            		if (curPopulation[i].getScore() > maxFit) {
            		    maxFit = curPopulation[i].getScore();
            		    indexBest = i;
            		}
      	    }

      	    // select according to description above for this method
      	    for (int i=0; i<popSize; i++) {
            		int selIndex = 0;
            		if (rand.nextInt(100) < selParam)  // rand less than param, select best individual
            		    {
            		    selIndex = indexBest;
            		    }
            		else  // otherwise select random individual
            		    {
            			selIndex = rand.nextInt(popSize);
            		    }
            		Selected[i] = (Prisoner)curPopulation[selIndex].clone();
      	    }
      	} else if (selection == 1) {
            Selected=fitnessProportionalSelection();
        } else if (selection == 2) {
            Selected=tournamentSelection();
        } else {  // any other selection method fill pop with always cooperate
      	    for (int i=0; i<popSize; i++)
      		      Selected[i] = new Prisoner("ALLC");
      	}


      	//Crossover & Mutate each pair of selected parents
      	BitSet Offspring[] = new BitSet[2];  // temporarily holds 2 children during crossover/mutation

        //don't mutate last of selected if we're using elitism
        int subPopSize = popSize;
        if (selection==1){
            subPopSize -= selParam;
        }
        int prisonersReplaced = 0;

      	for (int d=0; d<subPopSize; d+=2) {

              // in case of odd population, just mutate and replace last individual
                if (d+1 >= subPopSize){
              		Offspring[0] = Genetic.mutate(Selected[d].getStrat(), mutateP, rand);
              		Selected[d] = new Prisoner(Offspring[0]);
                  prisonersReplaced ++;
                }
        	      else {
                		if(rand.nextDouble() <= crossP) //Cross Over
                		    Offspring = Genetic.crossover(Selected[d].getStrat(),Selected[d+1].getStrat(), rand);
                		else { //clones
                  			Offspring[0] = (BitSet)Selected[d].getStrat().clone();
                  			Offspring[1] = (BitSet)Selected[d+1].getStrat().clone();
                		}

                		//Mutation
                		Offspring[0] = Genetic.mutate(Offspring[0],mutateP, rand);
                		Offspring[1] = Genetic.mutate(Offspring[1],mutateP, rand);

                		//Replacement - we are done with parents d & d+1, so just replace with children without
                		// creating an entire new array
                		Selected[d] = new Prisoner(Offspring[0]);
                		Selected[d+1] = new Prisoner(Offspring[1]);
                    prisonersReplaced ++;
                    prisonersReplaced ++;
        	    }
      	}
        if (subPopSize != popSize){
           for (int d=subPopSize; d<popSize; d+=1){
              Selected[d] = new Prisoner((BitSet)Selected[d].getStrat().clone());
              prisonersReplaced ++;
           }
        }

      	// pass on children pop to be parents of next gen
      	curPopulation = Selected;
      	repaint();	//update display (if any)
      	return curPopulation; //return the bred population
    }


    /**
     *Responsible for updating the graphical representation
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); //paint background

	      //Get limits of viewable area
      	Insets insets = getInsets();
      	int x0 = insets.left;
      	int y0 = insets.top;
      	int currentWidth = getWidth() - insets.left - insets.right;
      	int currentHeight = getHeight() - insets.top - insets.bottom;

      	//Display a series of rectangles, representing the players
      	for(int i = 0; i < popSize; i++) {
    	    	g.setColor(curPopulation[i].getColor());
    	    	g.fillRect((x0*2)+((currentWidth/popSize)*(i)),(currentHeight/4)+y0,(currentWidth/popSize),currentHeight/2);
	    }
    }

    /**
     *Set the {@link  ie.errity.pd.Rules Rules}
     *@param r new {@link  ie.errity.pd.Rules Rules}
     */
    public void setRules(Rules r)
    {
      	mutateP = r.getMutateP();
      	crossP = r.getCrossP();
      	selection = r.getSelection();
      	selParam = r.getSelectionParameter();
      	seed = r.getSeed();
      	if (seed > -1)
      	    rand.setSeed(seed);
    }

    /**
     *Reset the breeder
     */
    public void clear()
    {
      	popSize = 0;
      	repaint();
    }
}
