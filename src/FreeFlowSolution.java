import java.util.*;


public class FreeFlowSolution {
	
	public static final int TOP_EDGE = 1;
	public static final int LEFT_EDGE = 2;
	public static final int BOTTOM_EDGE = 3;
	public static final int RIGHT_EDGE = 4;
	
	public static final int NO_OBSTACLE_CLOCKWISE = 1;
	public static final int NO_OBSTACLE_ANTI_CLOCKWISE = 2;
	public static final int OBSTACLE_PRESENT = -1;
	
	static int[][] all_points = new int[5][2];
	static List<Integer> blockedPoints = new ArrayList<Integer>();
	static List<List<Integer>> solutionPaths = new ArrayList<>(5);
	static int[] mapIndexOfSolution = new int[5];
	static int[] priority = new int[5];

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
        Scanner s = new Scanner(System.in);
        for (int i = 0; i<5; i++) {
        	for (int j = 0; j<2; j++) {
        		all_points[i][j] = s.nextInt();
        		addToBlockedPoints(all_points[i][j]);
        	}
        	mapIndexOfSolution[i]=-1;		//Value -1 means the points are not linked yet
        	priority[i]=-1;					//Value -1 means priority is not required or not set
        }
        
        
        
        solveOnEdgePointsWithNoBarrier();
        
        setPriorityWithLeastDistancebetweenPoints();
		
        
        System.out.println(solutionPaths.toString());
		System.out.println(blockedPoints.toString());
		int i=0;
		while(i<5) {
			System.out.println(mapIndexOfSolution[i++]);
		}
		
		
	}

	private static void addToBlockedPoints(int point) {
		blockedPoints.add(point);
	}

	private static boolean pointNotBlocked(int point) {
		for(int i=0; i<blockedPoints.size(); i++) 
			if(blockedPoints.get(i) == point)
				return false;
		return true;
	}

	private static void addPathToSolution(List<Integer> path) {
		solutionPaths.add(path);
		setIndexOfSolution(path.get(0), solutionPaths.size()-1);
		blockPathPoints(path);

	}

	private static void blockPathPoints(List<Integer> path) {
		for(int i=1; i<path.size()-1; i++)
			addToBlockedPoints(path.get(i));
	}

	
	private static void setIndexOfSolution(Integer a, Integer index) {
		for(int i=0; i<5; i++) {
			if(all_points[i][0] == a)
				mapIndexOfSolution[i] = index;
		}
	}
	
	private static List<Integer> getIndexOfUnsolvedPaths() {
		int i=0;
		List<Integer> indexes = new ArrayList<Integer>();
		while(i<5) {
			if(mapIndexOfSolution[i] == -1)
				indexes.add(i);
			i++;
		}
		return indexes;
	}

	private static void setPriorityWithLeastDistancebetweenPoints() {
		List<Integer> indexOfUnsolvedPointsInAllPaths = getIndexOfUnsolvedPaths();
		int i=0, a, b;
		
		int[] minimumNumberOfHopsOfUnsolvedPaths = new int[indexOfUnsolvedPointsInAllPaths.size()];
		while(i<indexOfUnsolvedPointsInAllPaths.size()) {
			a = all_points[indexOfUnsolvedPointsInAllPaths.get(i)][0];
			b = all_points[indexOfUnsolvedPointsInAllPaths.get(i)][1];
			minimumNumberOfHopsOfUnsolvedPaths[i++] = Math.abs(((a-1)%5)-((b-1)%5)) + Math.abs(((a-1)/5)-((b-1)/5)) ;
		}
		int indexOfMin = 0;
		int min = minimumNumberOfHopsOfUnsolvedPaths[0];
		for(i=0; i<indexOfUnsolvedPointsInAllPaths.size(); i++) {
			System.out.println("Index "+ indexOfUnsolvedPointsInAllPaths.get(i) + " Min Path: "+ minimumNumberOfHopsOfUnsolvedPaths[i]);
			for(int j=1; j<indexOfUnsolvedPointsInAllPaths.size(); j++) {
				if(minimumNumberOfHopsOfUnsolvedPaths[j] < min) {
					min = minimumNumberOfHopsOfUnsolvedPaths[j];
					indexOfMin = j;
				}
				else if(minimumNumberOfHopsOfUnsolvedPaths[j] == min) {
					// TODO
				}
			}
			setNextPriority(indexOfUnsolvedPointsInAllPaths.get(indexOfMin));
		}
		
		
	}
	
	private static void setNextPriority(Integer integer) {
		// TODO Auto-generated method stub
		
	}

	private static void solveOnEdgePointsWithNoBarrier() {
		// TODO Auto-generated method stub
		for(int i=0; i<5; i++) {
			if(arePointsAreOnEdge(all_points[i])) {
				int hasObstacleOnEdge = pointsHaveNoObstacleOnEdge(all_points[i]);
				if(hasObstacleOnEdge == NO_OBSTACLE_ANTI_CLOCKWISE) {
					List<Integer> path = traverseAntiClockwiseOnEdgeUntilBIsFound(all_points[i][0], all_points[i][1]);
					addPathToSolution(path);
				}
				else if(hasObstacleOnEdge == NO_OBSTACLE_CLOCKWISE) {
					List<Integer> path = traverseClockwiseOnEdgeUntilBIsFound(all_points[i][0], all_points[i][1]);
					addPathToSolution(path);
				}
				else
					System.out.println(all_points[i][0] + " "+ all_points[i][1] +" have obstacles on edge");
			}
		}
	}

	private static int pointsHaveNoObstacleOnEdge(int[] points) {
		int a = points[0], b = points[1];
		List<Integer> path = traverseAntiClockwiseOnEdgeUntilBIsFound(a, b);
		if(path.get(path.size()-1) == b) {
			System.out.println(points[0]+" "+points[1]+": "+path.toString());
			return NO_OBSTACLE_ANTI_CLOCKWISE;
		}
		path = traverseClockwiseOnEdgeUntilBIsFound(a, b);
		if(path.get(path.size()-1) == b) {
			System.out.println(points[0]+" "+points[1]+": "+path.toString());
			return NO_OBSTACLE_CLOCKWISE;
		}
		System.out.println(points[0]+" "+points[1]+": "+path.toString());
		return OBSTACLE_PRESENT;
	}

	private static List<Integer> traverseClockwiseOnEdgeUntilBIsFound(int a, int b) {
		List<Integer> path = new ArrayList<Integer>();
		path.add(a);
		int edgeNameOfA = getEdgeName(a);
		while(path.get(path.size()-1) != b) {		// TODO: Check if changing this sentence to while(True) makes any difference or not
			switch(edgeNameOfA) {
				case TOP_EDGE:
					while(a!=5) {
						a++;
						path.add(a);
						if(!pointNotBlocked(a))	//Point is blocked
							return path;
					}
					edgeNameOfA = RIGHT_EDGE;
					break;
				case LEFT_EDGE:
					while(a!=1) {
						a-=5;
						path.add(a);
						if(!pointNotBlocked(a))	//Point is blocked
							return path;
					}
					edgeNameOfA = TOP_EDGE;
					break;
				case BOTTOM_EDGE:
					while(a!=21) {
						a--;
						path.add(a);
						if(!pointNotBlocked(a))	//Point is blocked
							return path;
					}
					edgeNameOfA = LEFT_EDGE;
					break;
				case RIGHT_EDGE:
					while(a!=25) {
						a+=5;
						path.add(a);
						if(!pointNotBlocked(a))	//Point is blocked
							return path;
					}
					edgeNameOfA = BOTTOM_EDGE;
					break;
			}
		}
		
		return path;
	}

	private static List<Integer> traverseAntiClockwiseOnEdgeUntilBIsFound(int a, int b) {
		List<Integer> path = new ArrayList<Integer>();
		path.add(a);
		int edgeNameOfA = getEdgeName(a);
		while(path.get(path.size()-1) != b) {		// TODO: Check if changing this sentence to while(True) makes any difference or not
			switch(edgeNameOfA) {
				case TOP_EDGE:
					while(a!=1) {
						a--;
						path.add(a);
						if(!pointNotBlocked(a))	//Point is blocked
							return path;
					}
					edgeNameOfA = LEFT_EDGE;
					break;
				case LEFT_EDGE:
					while(a!=21) {
						a+=5;
						path.add(a);
						if(!pointNotBlocked(a))	//Point is blocked
							return path;
					}
					edgeNameOfA = BOTTOM_EDGE;
					break;
				case BOTTOM_EDGE:
					while(a!=25) {
						a++;
						path.add(a);
						if(!pointNotBlocked(a))	//Point is blocked
							return path;
					}
					edgeNameOfA = RIGHT_EDGE;
					break;
				case RIGHT_EDGE:
					while(a!=5) {
						a-=5;
						path.add(a);
						if(!pointNotBlocked(a))	//Point is blocked
							return path;
					}
					edgeNameOfA = TOP_EDGE;
					break;
			}
		}
		
		return path;
	}

	private static int getEdgeName(int point) {
		if(point<=5)
			return TOP_EDGE;
		else if(point>=21)
			return BOTTOM_EDGE;
		else if(point%5==1)
			return LEFT_EDGE;
		else if(point%5==0)
			return RIGHT_EDGE;
		return -1;
	}

	private static boolean arePointsAreOnEdge(int[] points) {
		boolean startPointOnEdge = pointIsOnVerticalEdge(points[0]) || pointIsOnHorizontalEdge(points[0]);
		boolean endPointOnEdge = pointIsOnVerticalEdge(points[1]) || pointIsOnHorizontalEdge(points[1]);
		return startPointOnEdge && endPointOnEdge;
	}

	private static boolean pointIsOnHorizontalEdge(int point) {
		return (point<=5) || (point>=21);
	}

	private static boolean pointIsOnVerticalEdge(int point) {
		return (point%5==0) || (point%5==1);
	}

}
