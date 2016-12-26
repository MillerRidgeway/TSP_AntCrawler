# TSP_AntCrawler
A heuristic solution to the Traveling Salesmen Problem based on the paper found at:

http://people.idsia.ch/~luca/acs-bio97.pdf


A visualizer for the pre-set cities is built into the Mapper class, while text output is done through the Top class. Test Cities, located inside the Top class, can be used for debugging any changes to the core program.

All data is input through a CSV, and follows the format:

City_Name, Dist1, Dist2,,

The relationship of the cities is determined by the user, simply ensure that they all network together in the desired manner. This application utilizes Java Swing, and JDesktop. Please make sure both of these libraries are in the project path. If neither are required, simply disable the visualizer by removing the Mapper class.
