import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
public class Solution {
	static double MaxDistance = 2000000.0;	//distance can't be larger than this
    static double epsilon = 0.001;				//considered equal when different by less than this
	static double MaxCoord = 1000000.0;
	static int MaxTopNum = 10000;
	static int MaxQuestionNum = 1000;
	static int MaxQueryNum = 10000;
    public void Solution() throws Exception {
//		System.setIn(new FileInputStream("/home/wjy/workspace/programming challenge/STDIN"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //process imput
        String line = br.readLine();
        int numT, numQ, numN;
        String[] line1 = line.split(" ");
        numT = Integer.parseInt(line1[0]);	//number of topics
        numQ = Integer.parseInt(line1[1]);	//number of questions
        numN = Integer.parseInt(line1[2]);	//number of queries
        Topic[] ts = new Topic[MaxTopNum];		//topic array
        Question[] qs = new Question[MaxQuestionNum];	//question array
        NQuery[] ns = new NQuery[MaxQueryNum];		//query array
        
        //get topics
        HashMap<Integer, Integer> idtoindex = new HashMap<Integer, Integer>();
        for(int i = 0; i < numT; i++){
        	ts[i] = new Topic();
        	line = br.readLine();
        	line1 = line.split(" ");
        	ts[i].id = Integer.parseInt(line1[0]);
        	ts[i].p.x = Double.parseDouble(line1[1]);
        	ts[i].p.y = Double.parseDouble(line1[2]);
        	idtoindex.put(ts[i].id, i);
        }
        
        //put topics into 30*30 gridbucket, a little more than 10 in each bucket
        GridBucket<Topic> gb = new GridBucket<Topic>(30, 30);
        double step = MaxCoord / 30 + 1;
        for(int i = 0; i < numT; i++){
        	gb.bucket.get((int)(ts[i].p.x / step))  .get((int)(ts[i].p.y / step)).add(ts[i]);
        }
        
        //get questions
        
        HashMap<Integer, ArrayList<Question>> topIdToQ = new HashMap<Integer, ArrayList<Question>>();
        
        for(int i = 0; i < numQ; i++){
        	qs[i] = new Question();
        	line = br.readLine();
        	line1 = line.split(" ");
        	qs[i].id = Integer.parseInt(line1[0]);
        	int currentnum = Integer.parseInt(line1[1]);
        	qs[i].numT = currentnum;
        	for(int j = 0; j < currentnum; j++){
        		int topId = Integer.parseInt(line1[2 + j]);
        		qs[i].tIds[j] = topId;
        		
        		if(topIdToQ.containsKey(topId)){
        			topIdToQ.get(topId).add(qs[i]);
        		}
        		else{
        			ArrayList<Question> qa = new ArrayList<Question>();
        			qa.add(qs[i]);
        			topIdToQ.put(topId, qa);
        		}
        		
        	}
        }
        
        
        //remove questions with no topics
        int newnumQ = numQ;
        int index = 0;
    	for(int i = 0; i < numQ; i++){
    		if(qs[i].numT == 0){
    			newnumQ--;
    			continue;
    		}
    		qs[index].id = qs[i].id;
    		qs[index].numT = qs[i].numT;
    		qs[index].tIds = qs[i].tIds;
    		index++;
    	}
    	numQ = newnumQ;
        //get queries
        for(int i = 0; i < numN; i++){
        	ns[i] = new NQuery();
        	line = br.readLine();
        	line1 = line.split(" ");
        	ns[i].type = line1[0].charAt(0);
        	ns[i].numAnswers = Integer.parseInt(line1[1]);
        	ns[i].p.x = Double.parseDouble(line1[2]);
        	ns[i].p.y = Double.parseDouble(line1[3]);
        }
        //end of imput process
        //iterate on queries
        for(int i = 0; i < numN; i++){
        	if(ns[i].type == 't'){
        		answerT(ts, numT, ns[i], gb);
        	}
        	if(ns[i].type == 'q'){
        		answerQ(qs, ts, numQ, ns[i], idtoindex, gb, topIdToQ);
        	}
        	if(i < numN - 1){
        		System.out.println();
        	}
        }
        
        
    }    
    
    
    
    //method of calculating distance, use straight line distance here
    public double dist(Point p1, Point p2){
    	return Math.sqrt((p1.x - p2.x)*(p1.x - p2.x) + (p1.y - p2.y)*(p1.y - p2.y));
    }
    
    //calculate distance between a query and a topic
    public double distNT(NQuery n, Topic t){
    	return dist(n.p, t.p);
    }
    
    //calculate distance between a query and a question
    public double distNQ(NQuery n, Question q, Topic[] ts, HashMap<Integer, Integer> idtoindex){
    	double dist = MaxDistance;
    	for(int i = 0; i < q.numT; i++){
    		if(distNT(n, ts[idtoindex.get(q.tIds[i])]) < dist){
    			dist = distNT(n, ts[idtoindex.get(q.tIds[i])]);
    			q.p = ts[idtoindex.get(q.tIds[i])].p;
    		}
    	}
    	return dist;
    }
    
    //answer query about topic
    public void answerT(Topic[] ts, int numT, NQuery n, GridBucket<Topic> gb){
    	
    	double step = MaxCoord / 30 + 1;
    	int centreX = (int)(n.p.x / step);
    	int centreY = (int)(n.p.y / step);
    	double density = numT / 900;
    	int numOfGrid = (int)(400 / density + 1);
    	int radius = (int)(Math.sqrt(numOfGrid / 3.14) + 1);
    	ArrayList<Topic> newts = gb.findPie(centreX, centreY, radius);
    	int multiplier = 1;
    	while(newts.size() < Math.min(numT, 101)){
    		radius = radius*multiplier;
    		multiplier++;
    		newts = gb.findPie(centreX, centreY, radius);
    	}
    	int newnumT = newts.size();
    	TCouple[] tcs = new TCouple[newnumT];
    	for(int i = 0; i < newnumT; i++){
    		tcs[i] = new TCouple();
    		tcs[i].topicid = newts.get(i).id;
    		tcs[i].distance = distNT(n, newts.get(i));
    	}
    	
    	int capacity = Math.min(newnumT, 101);
    	
    	
    	
//    	//calculate distance between each topic and this query and put the 
//    	//topic id and distance couble into an array of TDCouple.
//    	//make TCouple array
//    	TCouple[] tcs = new TCouple[numT];
//    	//holding topics in cube
//    	TCouple[] tcsc = new TCouple[numT];
//    	//want to examine at least 101 topics. If numT > 1000 cut a pie
//    	//that's expected to hold 500 topics, so that the probability of 
//    	//less than 101 topics in the circle is small. should that happen, do whole search
//    	double radius = MaxCoord;
//    	if(numT > 400){
//    		double portion = 200 / numT;
//    		radius = Math.sqrt(portion) * MaxCoord;
//    	}
//    	//index for tcs
//    	for(int i = 0; i < numT; i++){
//    		tcs[i] = new TCouple();
//    		tcs[i].topicid = ts[i].id;
//    		tcs[i].distance = distNT(n, ts[i]);
//    	}
//    	int ind = 0;
//    	for(int i = 0; i < numT; i++){
//    		if(tcs[i].distance < radius){
//    			tcsc[ind] = tcs[i];
//    			ind++;
//    		}
//    	}
//    	if(ind >= 101){
//    		numT = ind;
//    		tcs = tcsc;
//    	}
    	
    	
    	
    	
    	//making maxheap, larger means large dist and small id
    	Comparator<TCouple> myc = new Comparator<TCouple>(){
    		@Override
    		public int compare(TCouple tc1, TCouple tc2){
    			if(mycompare(tc1.distance, tc2.distance) == 0){
    				if(tc1.topicid > tc2.topicid){
    					return -1;
    				}
    				else{
    					return 1;
    				}
    			}
    			else{
    				return mycompare(tc1.distance, tc2.distance);
    			}
    		}
    	};
//    	//capacity of the heap is min of questions and 101
//    	int capacity = Math.min(numT, 101);
    	
    	Myheap<TCouple> mh = new Myheap<TCouple>(capacity, myc);
    	//initialize mh with the first capacity TCouple
    	mh.buildheap(tcs);
    	//pop and add the rest
    	for(int i = capacity; i < numT; i++){
    		mh.slots.set(0, tcs[i]);
    		mh.maxheapify(0);
    	}
    	//pop the remainning one by one into array and give the first few
    	TCouple[] ans = new TCouple[capacity];
    	for(int i = 0; i < capacity; i++){
    		ans[i] = mh.slots.get(0);
    		mh.slots.set(0, mh.slots.get(mh.capacity - 1));
    		mh.capacity--;
    		mh.maxheapify(0);
    	}
    	int numToShow = Math.min(capacity, n.numAnswers);
    	for(int i = 0; i < numToShow; i++){
    		System.out.print(ans[capacity - i - 1].topicid + " ");
    	}
    	
    	
    	
    	
    	
    }
    
    //answer query about question, same as topic except don't count 0 topic questions
    public void answerQ(Question[] qs, Topic[] ts, int numQ, NQuery n, HashMap<Integer, Integer> idtoindex, GridBucket<Topic> gb, HashMap<Integer, ArrayList<Question>> topIdToQ){
    	
    	double step = MaxCoord / 30 + 1;
    	int centreX = (int)(n.p.x / step);
    	int centreY = (int)(n.p.y / step);
    	double density = ts.length / 900;
    	int numOfGrid = (int)(400 / density + 1);
    	int radius = (int)(Math.sqrt(numOfGrid / 3.14) + 1);
    	ArrayList<Topic> newts = gb.findPie(centreX, centreY, radius);
    	HashSet<Question> hs = new HashSet<Question>();
    	int multiplier = 1;
    	while(hs.size() < Math.min(numQ, 101)){
    		hs.clear();
    		radius = radius*multiplier;
    		multiplier++;
    		newts = gb.findPie(centreX, centreY, radius);
    		for(int i = 0; i < newts.size(); i++){
    			hs.addAll(topIdToQ.get(newts.get(i).id));
    		}
    	}
    	int newnumT = hs.size();
    	TCouple[] tcs = new TCouple[newnumT];
    	int index = 0;
    	for(Question q : hs){
    		tcs[index] = new TCouple();
    		tcs[index].topicid = q.id;
    		tcs[index].distance = distNQ(n, q, ts, idtoindex);
    		index++;
    	}
    	int capacity = Math.min(index, 101);
    	
//    	//calculate distance between each topic and this query and put the 
//    	//topic id and distance couble into an array of TDCouple.
//    	//make TCouple array
//    	TCouple[] tcs = new TCouple[numQ];
//    	//holding topics in cube
//    	TCouple[] tcsc = new TCouple[numQ];
//    	//want to examine at least 101 topics. If numT > 1000 cut a rectangular piece 
//    	//that's expected to hold 500 topics, so that the probability of 
//    	//less than 101 topics in the circle is small. should that happen, do whole search
//    	double radius = MaxCoord;
//    	if(numQ > 400){
//    		double portion = 200 / numQ;
//    		radius = Math.sqrt(portion) * MaxCoord;
//    	}
//    	//index for tcs
//    	for(int i = 0; i < numQ; i++){
//    		tcs[i] = new TCouple();
//    		tcs[i].topicid = qs[i].id;
//    		tcs[i].distance = distNQ(n, qs[i], ts, idtoindex);
//    	}
//    	int ind = 0;
//    	for(int i = 0; i < numQ; i++){
//    		if(tcs[i].distance < radius){
//    			tcsc[ind] = tcs[i];
//    			ind++;
//    		}
//    	}
//    	if(ind >= 101){
//    		numQ = ind;
//    		tcs = tcsc;
//    	}
    	
    	
    	
    	
    	//making maxheap, larger means large dist and small id
    	Comparator<TCouple> myc = new Comparator<TCouple>(){
    		@Override
    		public int compare(TCouple tc1, TCouple tc2){
    			if(mycompare(tc1.distance, tc2.distance) == 0){
    				if(tc1.topicid > tc2.topicid){
    					return -1;
    				}
    				else{
    					return 1;
    				}
    			}
    			else{
    				return mycompare(tc1.distance, tc2.distance);
    			}
    		}
    	};
    	
//    	//capacity of the heap is min of questions and 101
//    	int capacity = Math.min(numQ, 101);
    	
    	Myheap<TCouple> mh = new Myheap<TCouple>(capacity, myc);
    	//initialize mh with the first capacity TCouple
    	mh.buildheap(tcs);
    	//pop and add the rest
    	for(int i = capacity; i < numQ; i++){
    		mh.slots.set(0, tcs[i]);
    		mh.maxheapify(0);
    	}
    	//pop the remainning one by one into array and give the first few
    	TCouple[] ans = new TCouple[capacity];
    	for(int i = 0; i < capacity; i++){
    		ans[i] = mh.slots.get(0);
    		mh.slots.set(0, mh.slots.get(mh.capacity - 1));
    		mh.capacity--;
    		mh.maxheapify(0);
    	}
    	int numToShow = Math.min(capacity, n.numAnswers);
    	for(int i = 0; i < numToShow; i++){
    		System.out.print(ans[capacity - i - 1].topicid + " ");
    	}
    	
    	
    	
    	
    	
    }
    
    //method to compare two doubles: -1 <, 0 =, 1 >
    private int mycompare(double d1, double d2){
    	//d1 = d2
    	if((d1 - d2) < epsilon && (d2 - d1) < epsilon){
    		return 0;
    	}
    	//d1 > d2
    	else if((d1 - d2) > epsilon){
    		return 1;
    	}
    	//d1 < d2
    	else{
    		return -1;
    	}
    }
    
    public static void main(String[] args) {
		Solution s = new Solution();
		try{
			s.Solution();
		}catch(Exception e){
			System.out.println(e.getStackTrace());
		}
	}
    
    
    
}



class TCouple{
	int topicid;
	double distance;
	public String toString(){
		return Integer.toString(topicid);
	}
	
}

class Point{
	public double x;
	public double y;
}

class Topic{
	public int id;
	//entity location
	public Point p = new Point();
	
}

class Question{
	public int id;
	//number of related topics
	public int numT;
	//ids of related topics
	public int[] tIds = new int[10];
	public Point p;
	
}

class NQuery{
	char type;
	//number of answers demanded
	int numAnswers;
	//query location
	public Point p = new Point();
	
}

class Myheap<T>{
	int capacity;
	Comparator<T> c;
	ArrayList<T> slots = new ArrayList<T>();
	
	Myheap(int capacity, Comparator<T> c){
		this.capacity = capacity;
		this.c = c;
	}
	
	//imputs should be T[] of min value
	void buildheap(T[] imputs){
		for(int i = 0; i < capacity; i++){
			slots.add(imputs[i]);
		}
		for(int i = (capacity-1)/2; i >= 0; i--){
			maxheapify(i);
		}
	}
	
	void maxheapify(int i){
		int l = left(i);
		int r = right(i);
		int largest;
		if(l < capacity && c.compare(slots.get(l), slots.get(i)) > 0){
			largest = l;
		}
		else{
			largest = i;
		}
		if(r < capacity && c.compare(slots.get(r), slots.get(largest)) > 0){
			largest = r;
		}
		if(largest != i){
			T temp = slots.get(i);
			slots.set(i, slots.get(largest));
			slots.set(largest, temp);
			maxheapify(largest);
		}
	}
	
	boolean check(){
		for(int i = 0; i < capacity; i++){
			int r = right(i);
			int l = left(i);
			if(l < capacity && c.compare(slots.get(l), slots.get(i)) > 0){
				return false;
			}
			if(r < capacity && c.compare(slots.get(r), slots.get(i)) > 0){
				return false;
			}
		}
		return true;
	}
	
	static boolean inttest(int capacityrange, int times){
		Random rand = new Random(47);
		for(int i = 0; i < times; i++){
			int cap = rand.nextInt(capacityrange);
			
			//need to change method to make a
			Integer[] a = new Integer[cap];
			for(int j = 0; j < cap; j++){
				a[j] = rand.nextInt(capacityrange);
			}
			//need to change comparator
			Myheap<Integer> mh = new Myheap<Integer>(cap, new Comparator<Integer>(){
				@Override
				public int compare(Integer a, Integer b){
					return a-b;
				}
			});
			mh.buildheap(a);
			if(!mh.check()){
				return false;
			}
			
		}
		return true;
	}
	
	int parent(int i){
		return (i-1)/2;
	}
	
	int left(int i){
		return 2*i+1;
	}
	
	int right(int i){
		return 2*i+2;
	}
	
}

class GridBucket<T>{
	ArrayList<ArrayList<ArrayList<T>>> bucket = new ArrayList<ArrayList<ArrayList<T>>>();
	int[][] gridCoor;
	int xnum, ynum;
	GridBucket(int xnum, int ynum){
		this.xnum = xnum;
		this.ynum = ynum;
		for(int i = 0; i < xnum; i++){
			bucket.add(new ArrayList<ArrayList<T>>());
			for(int j = 0; j < ynum; j++){
				bucket.get(i).add(new ArrayList<T>());
			}
		}
		gridCoor = new int[xnum][ynum];
	}
	
	ArrayList<T> findPie(int centreX, int centreY, int radius){
		ArrayList<T> res = new ArrayList<T>();
		for(int i = 0; i < xnum; i++){
			for(int j = 0; j < ynum; j++){
				if(dist(i, j, centreX, centreY) < radius){
					res.addAll(bucket.get(i).get(j));
				}
			}
		}
		return res;
	}
	
	double dist(int x1, int y1, int x2, int y2){
		return Math.sqrt((double)((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2)));
	}
	
}









