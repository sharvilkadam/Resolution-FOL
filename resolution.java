import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;


class Node{
	private Node left,right;
	private String data;
	public Node()
	{
		
	}
	public Node(String s)
	{
		this.data=s;
	}
	public void setData(String t)
	{
		data=t;
	}
	public void setLeft(Node t)
	{
		left=t;
	}
	public void setRight(Node t)
	{
		right=t;
	}
	public String getData()
	{
		return data;
	}
	public Node getLeft()
	{
		return left;
	}
	public Node getRight()
	{
		return right;
	}
}

class Tree
{
	private Node root;
	 static Node prev;
	
	void setRoot(Node t)
	{
		root=t;
	}
	Node getRoot()
	{
		return root;
	}
	void printTree(Node r)
	{
		if(r==null)
			return;
		//System.out.print("(");
		printTree(r.getLeft());
		System.out.print(r.getData()+"p");
		printTree(r.getRight());
		//System.out.print(")");
	}
	void removeImplies(Node r)
	{
		if(r==null)
			return;
		removeImplies(r.getLeft());
		if(r.getData().equals("=>"))
		{
			Node not=new Node();
			not.setData("~");
			not.setRight(r.getLeft());
			not.setLeft(new Node(""));
			r.setLeft(not);
			r.setData("|");
		}
		removeImplies(r.getRight());
	}
	void moveNot(Node r)
	{
		if(r==null)
			return;
		//System.out.println(this.root.getData());
		if(this.root.getData().equals("~"))
		{
			//System.out.println("ko");
			Node node0=removeNot(r.getRight());
			this.root=node0;
		}
		if(r.getLeft()!=null && r.getLeft().getData().equals("~"))
		{
			Node node1=removeNot(r.getLeft().getRight());
			r.setLeft(node1);
			
		}
		if(r.getRight()!=null && r.getRight().getData().equals("~"))
		{
			Node node2=removeNot(r.getRight().getRight());
			r.setRight(node2);
		}
		
		moveNot(r.getLeft());
		moveNot(r.getRight());
	}
	Node removeNot(Node n)
	{
		if(Character.isUpperCase(n.getData().charAt(0)))	//if predicate
		{
			//System.out.println("o1");
			String p="~"+n.getData();
			n.setData(p);
			return n;
		}
		else if(n.getData().equals("~"))
		{
			return n.getRight();
		}
		else
		{
			if(n.getData().equals("&"))
				n.setData("|");
			else
				n.setData("&");
			Node r1=removeNot(n.getLeft());
			n.setLeft(r1);
			Node r2=removeNot(n.getRight());
			n.setRight(r2);
			return n;
		}
		//return null;
	}
	
	boolean checkAnd(Node r)
	{
		if(r==null)
			return false;
		if(r.getData().equals("&"))
			return true;
		return (checkAnd(r.getLeft()) || checkAnd(r.getRight()));
	}
	
	void moveAnd(Node r)
	{
		if(r==null)
			return;
		else
		{
			if(r.getData().equals("|") && r.getRight().getData().equals("&"))
			{
				Node cn=cloneNode(r.getLeft());
				Node n1=new Node("|");
				n1.setLeft(r.getLeft());
				n1.setRight(r.getRight().getLeft());
				r.setLeft(n1);
				r.setData("&");
				r.getRight().setData("|");
				r.getRight().setLeft(cn);
			}
			else if(r.getData().equals("|") && r.getLeft().getData().equals("&"))
			{
				Node cn=cloneNode(r.getRight());
				Node n1=new Node("|");
				n1.setRight(r.getRight());
				n1.setLeft(r.getLeft().getRight());
				r.setRight(n1);
				r.setData("&");
				r.getLeft().setData("|");
				r.getLeft().setRight(cn);
			}
			moveAnd(r.getLeft());
			moveAnd(r.getRight());
		}
	}
	Node cloneNode(Node r)
	{
		if(r==null)
			return null;
		if(r.getData().charAt(0)=='~' || Character.isUpperCase(r.getData().charAt(0)))
			return new Node(r.getData());
		Node n0=new Node(r.getData());
		n0.setLeft(cloneNode(r.getLeft()));
		n0.setRight(cloneNode(r.getRight()));
		return n0;
	}
	
	String getStanString(Node r,int n)
	{
		if(r==null)
			return "";
		if(Character.isUpperCase(r.getData().charAt(0)) || r.getData().charAt(0)=='~')	//if predicate
		{
			String nd=r.getData();
			int q1=nd.indexOf("(");
			int q2=nd.indexOf(")");
			String m=nd.substring(0,q1);
			String nd2=nd.substring(q1+1, q2);
			String nd3[]=nd2.split(",");
			String nd4="";
			for(int i=0;i<nd3.length;i++)
			{
				if(!Character.isUpperCase(nd3[i].charAt(0)))
				{
					nd3[i]=nd3[i]+n;
				}
				nd4=nd4+nd3[i]+",";
			}
			nd4=nd4.substring(0, nd4.length()-1);
			m=m+"("+nd4+")";
			
			return m;
		}
		
		return getStanString(r.getLeft(), n)+r.getData()+getStanString(r.getRight(), n);
	}
}

public class homework {
	static int qn;
	static String qs[];
	static int kn;
	static String ks[];
	static HashMap<String,ArrayList<Integer>> map = new HashMap<String,ArrayList<Integer>>();
	static List<String> kb=new ArrayList<String>();
	static long timeconstant=10000;
	
	
	static void generateKB(String s)
	{
		//System.out.println(s);
		Tree t=new Tree();
		Node r=new Node();
		t.setRoot(r);
		Stack<String> sop=new Stack<String>();
		Stack<Node> sno=new Stack<Node>();
		for(int i=0;i<s.length();i++)
		{
			
			char c=s.charAt(i);
			//System.out.print(c+" ");
			if(c=='(' || c=='|' || c=='&' || c=='~')
			{
				sop.push(c+"");
			}
			else if(c=='=')
			{
				sop.push(c+">");
				i++;
			}
			else if(Character.isUpperCase(c))
			{
				StringBuilder s1=new StringBuilder();
				int j=i;
				while(s.charAt(j)!=')')
				{
					s1.append(s.charAt(j));
					j++;
					i++;
				}
				//i++;
				s1.append(")");
				String s2=new String(s1);
				Node n1=new Node();
				n1.setData(s2);
				sno.push(n1);
			}
			else if(c==')')
			{
				String op=sop.pop();
				sop.pop();	//pop the ( bracket
				Node n2=new Node();
				n2.setData(op);
				t.setRoot(n2);
				Node nr=sno.pop();
				Node nl;
				//if(sno.empty()){
				if(op.equals("~")){
					nl=new Node();
					nl.setData("");
				}
				else
					nl=sno.pop();
				n2.setRight(nr);
				n2.setLeft(nl);
				sno.push(n2);
			}
		}
		if(!sno.empty())
			t.setRoot(sno.pop());
		//System.out.println();
		//System.out.println(t.getRoot().getLeft().getData());
		//t.printTree(t.getRoot());
		t.removeImplies(t.getRoot());
		//System.out.println();
		//t.printTree(t.getRoot());
		t.moveNot(t.getRoot());
		//System.out.println();
		//t.printTree(t.getRoot());
		
		/*if(t.checkAnd(t.getRoot()))
		{
			//System.out.println("conatins and");
			while(!t.getRoot().getData().equals("&"))
			{
				//System.out.println(" insi");
				t.moveAnd(t.getRoot());
			}
			Tree t2=new Tree();
			t2.setRoot(t.getRoot().getRight());
			t.setRoot(t.getRoot().getLeft());
			System.out.println("Tree1:");
			t.printTree(t.getRoot());
			System.out.println("\nTree2");
			t2.printTree(t2.getRoot());
		}*/
		
		List<Tree> tarray=new ArrayList<Tree>();
		tarray.add(t);
		for(int i=0;i<tarray.size();i++)
		{
			Tree d=tarray.get(i);
			if(d.checkAnd(d.getRoot()))
			{
				//System.out.println("conatins and");
				while(!d.getRoot().getData().equals("&"))
				{
					//System.out.println(" insi");
					d.moveAnd(d.getRoot());
				}
				Tree d2=new Tree();
				d2.setRoot(d.getRoot().getRight());
				d.setRoot(d.getRoot().getLeft());
				/*System.out.println("Tree1:");
				t.printTree(t.getRoot());
				System.out.println("\nTree2");
				t2.printTree(t2.getRoot());*/
				tarray.remove(i);
				tarray.add(d);
				tarray.add(d2);
				i--;
			}
		}
		/*for(int i=0;i<tarray.size();i++)
		{
			System.out.println("Tree: "+i);
			tarray.get(i).printTree(tarray.get(i).getRoot());
		}*/
		//tarray contains all the CNF sentences..
		//System.out.println();
		//System.out.println("Standa");
		for(int i=0;i<tarray.size();i++)
		{
			Tree dum=tarray.get(i);
			String sen=dum.getStanString(dum.getRoot(),kb.size());
			kb.add(sen);
			//System.out.println(sen);
		}
		
	}
	
	/*static void generateKB1()
	{
		for(int i=0;i<ks.length;i++)
		{
			if(ks[i].contains("=>"))		//implies
			{
				
			}
			
			if(ks[i].contains("|") && !ks[i].contains("&"))
			{
				
			}
			else if(ks[i].contains("|") && !ks[i].contains("&"))
			{
				
			}
			else if(ks[i].contains("|") && !ks[i].contains("&"))
			{
				
			}
			else		//single literal
			{
				kb.add(ks[i]);
				String pred=ks[i].substring(0, ks[i].indexOf("("));
				if(map.containsKey(pred))	//if the hashmap containg the predicate then append to the list
				{
					map.get(pred).add(kb.indexOf(ks[i]));
				}
				else	//create new list
				{
					ArrayList<Integer> nu=new ArrayList<Integer>();
					nu.add(kb.indexOf(ks[i]));
					map.put(pred, nu);
				}
				
			}
		}
	}*/
	
	static void displayMap(){
	    for (Entry<String, ArrayList<Integer>> entry : map.entrySet()) {
	        System.out.print(entry.getKey()+" | ");
	        for(int no : entry.getValue()){
	            System.out.print(no+" ");
	        }
	        System.out.println();
	    }
	}
	static void displayKB(){
		System.out.println("KB:");
	    for (int i=0;i<kb.size();i++) {
	        System.out.print(kb.get(i));
	        System.out.println();
	    }
	}
	static void displayMap2(HashMap<String,ArrayList<Integer>> m){
	    for (Entry<String, ArrayList<Integer>> entry : m.entrySet()) {
	        System.out.print(entry.getKey()+" | ");
	        for(int no : entry.getValue()){
	            System.out.print(no+" ");
	        }
	        System.out.println();
	    }
	}
	static void displayKB2(List<String> k){
		System.out.println("KB:");
	    for (int i=0;i<k.size();i++) {
	        System.out.print(k.get(i));
	        System.out.println();
	    }
	}
	
	static void addToMap(String a,int n)
	{
		
		String a1[]=a.split("\\|");
		for(int i=0;i<a1.length;i++)
		{
			String a2=a1[i];
			String pred=a2.substring(0, a2.indexOf("("));
			if(map.containsKey(pred))	//if the hashmap containg the predicate then append to the list
			{
				map.get(pred).add(n);
			}
			else	//create new list
			{
				ArrayList<Integer> nu=new ArrayList<Integer>();
				nu.add(n);
				map.put(pred, nu);
			}
		}
	}
	static void addToMap2(HashMap<String,ArrayList<Integer>> m,String a,int n)
	{
		String a1[]=a.split("\\|");
		for(int i=0;i<a1.length;i++)
		{
			String a2=a1[i];
			String pred=a2.substring(0, a2.indexOf("("));
			if(m.containsKey(pred))	//if the hashmap containg the predicate then append to the list
			{
				m.get(pred).add(n);
			}
			else	//create new list
			{
				ArrayList<Integer> nu=new ArrayList<Integer>();
				nu.add(n);
				m.put(pred, nu);
			}
		}
	}
	
	static boolean evaluateQuery(String qs)
	{
		
		List<String> ckb=new ArrayList<String>();
		for(int i=0;i<kb.size();i++)
			ckb.add(kb.get(i));
		HashMap<String,ArrayList<Integer>> newmap=(HashMap<String,ArrayList<Integer>>)map.clone();
		//displayMap2(newmap);
		String nq="";
		if(qs.charAt(0)=='~')
			nq=qs.substring(1);
		else
			nq="~"+qs;
		ckb.add(nq);
		/*for (int i=0;i<ckb.size();i++) {
	        System.out.println(ckb.get(i)); 
	    }*/
		//System.out.println(ckb.indexOf(nq));
		int index1=ckb.indexOf(nq);
		//int index2=ckb.indexOf(nq);
		long startTime = System.currentTimeMillis();
		do{					//loop1
			
			//System.out.println("index: "+index1);
			String s1=ckb.get(index1);		//s1 is the sentence to resolve later
			//System.out.println("s1"+s1);
			String sarray[]=s1.split("\\|");
			for(int i=0;i<sarray.length;i++)		//loop2
			{
				String c1=sarray[i];		//clause1 for unification
				//System.out.println(c1);
				String p=c1.substring(0, c1.indexOf("("));
				String np="";
				if(p.charAt(0)=='~')
					np=p.substring(1);
				else
					np="~"+p;
				//System.out.println(np);
				if(newmap.containsKey(np))
				{
					ArrayList<Integer> ia=newmap.get(np);
					/*for(int o=0;o<ia.size();o++)
						System.out.println(ia.get(o));*/
					for(int i2=0;i2<ia.size();i2++)		//loop3
					{
						if(ia.get(i2)>index1)
							continue;
						String s2=ckb.get(ia.get(i2));		//S2 is the second sentence to resolve later
						//System.out.println("s2="+s2);
						String s2array[]=s2.split("\\|");
						//System.out.println(s2array.length);
						for(int i3=0;i3<s2array.length;i3++)	//loop4
						{
							String c2=s2array[i3];
							String p2=c2.substring(0, c2.indexOf("("));
							//System.out.println("p2= "+p2);
							if(p2.equals(np))	//unify only if p2 is not of p ie. A(x) and ~A(x)
							{
								String theta=unify(c1,c2,"");
								//System.out.println("The"+theta);
								if(!theta.equals("FAILURE")) //&& !theta.equals(""))
								{
									if(!theta.equals(""))
										theta=theta.substring(0,theta.length()-1);	//removing extra ,
									
									String ss1=substitute(s1,theta);
									String ss2=substitute(s2,theta);
									//System.out.println("ss1="+ss1+" ss2="+ss2);
									String resolvant=resolve(ss1,ss2);
									//System.out.println("Resolvant="+resolvant);
									if(resolvant.equals(""))
										return true;
									else
									{
										//add to kb 
										//update map
										//standardized
										
										int si=ckb.size();
										String sr=stan(resolvant,si);
										//System.out.println("Sr "+sr);
										if(!ckb.contains(sr))
											ckb.add(sr);
										int mp=ckb.indexOf(resolvant);
										addToMap2(newmap, resolvant, mp);
									}
								}
							}
						}
					}
				}
			}
			index1++;
			long estimatedTime = System.currentTimeMillis() - startTime;
			if(estimatedTime>timeconstant)
				return false;
		}while(index1!=ckb.size());
		
		return false;
	}
	
	static String stan(String s,int n)
	{
		String ss[]=s.split("\\|");
		for(int k=0;k<ss.length;k++)
		{
			String s0=ss[k].substring(0, ss[k].indexOf("("));
			String s1=ss[k].substring(ss[k].indexOf("(")+1, ss[k].indexOf(")"));
			String sa[]=s1.split(",");
			for(int i=0;i<sa.length;i++)
			{
				if(Character.isLowerCase(sa[i].charAt(0)))
				{
					sa[i]=sa[i]+n;
				}
			}
			s0=s0+"(";
			for(int i=0;i<sa.length;i++)
			{
				s0=s0+sa[i]+",";
			}
			s0=s0.substring(0,s0.length()-1);
			s0=s0+")";
			ss[k]=s0;
		}
		String fs="";
		for(int i=0;i<ss.length;i++)
		{
			fs=fs+ss[i]+"|";
		}
		fs=fs.substring(0, fs.length()-1);
		return fs;
		
	}
	
	static String unify(String a,String b,String theta)
	{
		//System.out.println("u"+theta);
		String a1,b1="";
		if(a.charAt(0)=='~')
			a1=a.substring(1);
		else
			a1=a;
		if(b.charAt(0)=='~')
			b1=b.substring(1);
		else
			b1=b;
		if(a1.equals(b1))
			return theta;
		else
		{
			String a2=a1.substring(a1.indexOf("(")+1, a1.indexOf(")"));
			String b2=b1.substring(b1.indexOf("(")+1, b1.indexOf(")"));
			String aa[]=a2.split(",");
			String bb[]=b2.split(",");
			for(int i=0;i<aa.length;i++)
			{
				//System.out.println("aa="+aa[i]+"bb="+bb[i]);
				if(!aa[i].equals(bb[i]))
				{
					if(Character.isLowerCase(aa[i].charAt(0)) )//&& Character.isUpperCase(bb[i].charAt(0)))	
					{		//ie aa it is a variable and bb is a constant
						String t1=theta+aa[i]+"/"+bb[i];
						//System.out.println(t1);
						theta=t1+",";
						return unify(substitute(a, t1),substitute(b, t1),theta);
						
					}
					else if(Character.isLowerCase(bb[i].charAt(0)) )//&& Character.isUpperCase(aa[i].charAt(0)))	//ie bb it is a variable
					{
						String t1=theta+bb[i]+"/"+aa[i];
						//System.out.println(t1);
						theta=t1+",";
						return unify(substitute(a, t1),substitute(b, t1),theta);
					}
					else
					{
						return "FAILURE";
					}
				}
			}
			return "";
		}
	}
	
	static String resolve(String a,String b)
	{
		String resolvant="";
		String aa[]=a.split("\\|");
		String bb[]=b.split("\\|");
		ArrayList<String> al=new ArrayList<String>();
		ArrayList<String> bl=new ArrayList<String>();
		ArrayList<String> rl=new ArrayList<String>();
		for(int i=0;i<aa.length;i++)
			al.add(aa[i]);
		for(int i=0;i<bb.length;i++)
			bl.add(bb[i]);
		for(int i=0;i<al.size();i++)
		{
			for(int j=0;j<bl.size();j++)
			{
				//System.out.println(al.get(i)+"..."+bl.get(j));
				if(al.get(i).equals("~"+bl.get(j)) || ("~"+al.get(i)).equals(bl.get(j)))
				{
					al.remove(i);
					bl.remove(j);
					i--;
					break;
				}
			}
		}
		for(int i=0;i<al.size();i++)
		{
			if(!rl.contains(al.get(i)))
				rl.add(al.get(i));
		}
		for(int i=0;i<bl.size();i++)
		{
			if(!rl.contains(bl.get(i)))
				rl.add(bl.get(i));
		}
		for(int i=0;i<rl.size();i++)
		{
			resolvant=resolvant+rl.get(i)+"|";
		}
		if(!resolvant.equals(""))
			resolvant=resolvant.substring(0,resolvant.length()-1);
		return resolvant;
	}
	
	
	
	static String substitute(String a,String theta)
	{
		//System.out.println("Sub "+a);
		if(theta.equals(""))
			return a;
		String aarray[]=a.split("\\|");
		for(int k=0;k<aarray.length;k++)
		{
			String main=aarray[k].substring(0,aarray[k].indexOf("("));
			String ans=aarray[k].substring(aarray[k].indexOf("(")+1,aarray[k].indexOf(")" ));
			String a1[]=ans.split(",");
			String t[]=theta.split(",");
			for(int i=0;i<t.length;i++)
			{
				String t2[]=t[i].split("/");
				String r1=t2[0];
				String r2=t2[1];
				for(int j=0;j<a1.length;j++)
				{
					if(a1[j].equals(r1))
						a1[j]=r2;
				}
				//ans=ans.replaceAll(r1, r2);
			}
			main=main+"(";
			for(int i=0;i<a1.length;i++)
			{
				main=main+a1[i]+",";
			}
			main=main.substring(0, main.length()-1);
			main=main+")";
			aarray[k]=main;
		}
		String fs="";
		for(int i=0;i<aarray.length;i++)
		{
			fs=fs+aarray[i]+"|";
		}
		fs=fs.substring(0, fs.length()-1);
		return fs;
	}
	
	
	public static void main(String arg[]){
		
		FileInputStream fstream;
		List<String> l1=new ArrayList<String>();
		try {
			fstream = new FileInputStream("input.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				//System.out.println (strLine);
				l1.add(strLine);
			}
			br.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(l1);
		qn=Integer.parseInt(l1.get(0));
		qs=new String[qn];
		for(int i=0;i<qn;i++)
		{
			String q=l1.get(i+1).replaceAll("\\s+", "");
			if(q.contains("NOT"))
				q=q.replace("NOT", "~");
			if(q.contains("OR"))
				q=q.replace("OR", "|");
			if(q.contains("AND"))
				q=q.replace("AND", "&");
			if(q.contains("IMPLIES"))
				q=q.replace("IMPLIES", "=>");
			qs[i]=q;
		}
		kn=Integer.parseInt(l1.get(qn+1));
		ks=new String[kn];
		for(int i=0;i<kn;i++)
		{
			String q=l1.get(i+qn+2).replaceAll("\\s+", "");
			if(q.contains("NOT"))
				q=q.replace("NOT", "~");
			if(q.contains("OR"))
				q=q.replace("OR", "|");
			if(q.contains("AND"))
				q=q.replace("AND", "&");
			if(q.contains("IMPLIES"))
				q=q.replace("IMPLIES", "=>");
			ks[i]=q;
		}
		/*System.out.println(qn);
		for(int i=0;i<qs.length;i++)
		{
			System.out.println(qs[i]);
		}
		System.out.println(kn);
		for(int i=0;i<ks.length;i++)
		{
			System.out.println(ks[i]);
		}*/
		//generate KB array list
		for(int i=0;i<ks.length;i++)
		{
			//System.out.println(ks[i]);
			generateKB(ks[i]);
		}
		for(int i=0;i<kb.size();i++)
		{
			addToMap(kb.get(i),i);
		}
		//generateKB("((A(x)=>B(y))=>C(z))");
		//generateKB("((~(Parent(x,y) & Ancestor(y,z))) | Ancestor(x,z))");
		//generateKB("(~A(x))");
		//generateKB("(D(x,y)=>(~H(y)))");
		//displayData();
		//displayKB();
		//displayMap();
		//String u=unify("parents(x,y2,Chill)", "~parents(Bill,Father,y)", "");
		//String u=unify("A(x)","~B(Mama)", "");
		//String u=unify("~Ancestor(Liz,Billy)","Ancestor(x4,y4)","");
		//System.out.println("Theta="+u);
		//String r=resolve("A(Mama)|B(w)|A(x)","~A(Mama)|A(x)|~B(w)");
		//System.out.println("Resolvant="+r);
		//System.out.println(substitute("~Parents(x,y2,Chill)","x/Bill,y2/Ill"));
		//String q="";
		//q="F(Bob)";
		//q="Q(Bob)";
		//q="Ancestor(Liz,Billy)";
		//q="Ancestor(Liz,Bob)";
		/*if(evaluateQuery(q))
			System.out.println("TRUE");
		else
			System.out.println("FALSE");*/
		//System.out.println(substitute(q,""));
		//System.out.println(stan("Parents(x,y2,Chill)",14));
		/*for(int i=0;i<qn;i++)
		{
			if(evaluateQuery(qs[i]))
				System.out.println("TRUE");
			else
				System.out.println("FALSE");
		}*/
		PrintWriter writer;
		try {
			writer = new PrintWriter("output.txt");
			for(int i=0;i<qn;i++)
			{
				if(evaluateQuery(qs[i]))
					writer.println("TRUE");
				else
					writer.println("FALSE");
			}
			writer.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
