files changed :
    Breeder.java

pre-existing functions changed:
    Breed
    - added code to handle selection == 1 and 2
    - mutation + crossover portion allows for elitism in fitness proportional selection

added functions:
    - fitnessProportionalSelection
    - getScaledFitness
    - stochasticUniversal
    - bestPrisoner
    - tournamentSelection

other notes
    - now using ArrayLists in some functions
    - using Collections to shuffle
