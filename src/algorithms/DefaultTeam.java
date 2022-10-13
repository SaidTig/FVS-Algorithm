package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class DefaultTeam {

	public ArrayList<Point> calculFVS(ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> fvs = new ArrayList<Point>(); //les points que je supprime
		Evaluation E = new Evaluation();
		ArrayList<Point> fix =  new ArrayList<Point>(); //les points que je garde (on ne garde rien initialement)
		ArrayList<Point> fvsBis =  new ArrayList<Point>(); //une pseudo liste de noeuds supprimes
		for(int i=0;i<points.size();i++) fvsBis.add(points.get(i)); //elle contient tout les tous les points initialement
		
		//dans un premier temps je garde le max de points avec les plus petits degres qui donnent une validation
		while (fix.size()+fvs.size() < points.size()) {
			int d = points.size();	//pour trouver le plus petit degre
			int r = 0;	//indice du point qu'on veut garder
			for (int i=0; i<fvsBis.size(); i++) { //je prend le plus petit degre grace a cette boucle
				if (degree(fvsBis.get(i), points, edgeThreshold) < d) {
					d = degree(fvsBis.get(i), points, edgeThreshold);
					r = i;
				}
			}
			Point p = fvsBis.get(r); //j'enregistre le point que je vais prendre
			fvsBis.remove(fvsBis.get(r)); //je le supprime de la pseudo liste des points elimines
			ArrayList<Point> common = new ArrayList<Point>(); //me permet de concatener la liste des points supprimes et la pseudo liste
			common.addAll(fvsBis);
			common.addAll(fvs);
			HashSet<Point> hset = new HashSet<Point>(common);	//pour prendre les elements uniques
			ArrayList<Point> common2 = new ArrayList<>(hset); //reconversion en arraylist
			if(E.isValid(points, common2, edgeThreshold)) fix.add(p);	//ajouter le point au graphe s'il est bon
			else fvs.add(p);	//eliminer le point s'il est pas valide
		}
    
		ArrayList<Point> fvsBis2 =  new ArrayList<Point>();
		for (int i = 0; i<fvs.size(); i++) fvsBis2.add(fvs.get(i));
		//----------------------------------------------------------
		//maintenant, je recupere 2 points elimines et j'elimine un seul
		//pour chaque point que j'ai pas encore elimine, je trouve s'il a deux voisins qui sont elimines
		//si je change ce point avec ces deux voisins elimines et que c'est valide alors je le fait
		for (int i = 0; i<fix.size(); i++) {
			Point p1 = new Point();
			Point p2 = new Point();
			int n = 0; //compte les points que j'ai pris (un ou 2)
			for (int j = 0; j<fvs.size(); j++) { //choix des deux voisins
				if (isEdge(fix.get(i),fvs.get(j),edgeThreshold) && n==0) {p1 = fvs.get(j); n++;}
				if (isEdge(fix.get(i),fvs.get(j),edgeThreshold) && n==1 && fvs.get(j)!=p1) {p2 = fvs.get(j); n++;}
							
			}
			if(n==2) {
				fvsBis2.remove(p1);
				fvsBis2.remove(p2);
				fvsBis2.add(fix.get(i));
				if (E.isValid(points, fvsBis2, edgeThreshold)) {
					fvs.remove(p1);
					fvs.remove(p2);
					fvs.add(fix.get(i));
					System.out.println("tebdila");
				}
				else {
					fvsBis2.add(p1);
					fvsBis2.add(p2);
					fvsBis2.remove(fix.get(i));
				}
			}
		}
		//-------------------------------------------
		//maintenant, je parcours la liste des points elimines et je recupere le point s'il ne viole pas la validation
		ArrayList<Point> fvsBis3 =  new ArrayList<Point>();
		for (int i = 0; i<fvs.size(); i++) fvsBis3.add(fvs.get(i));
		
		for (Point p:fvs) {
			fvsBis3.remove(p);
			if (E.isValid(points, fvsBis3, edgeThreshold)) {fvs.remove(p); System.out.println("eeee");}
		}
		

		return fvs;
	}
	
	
	private boolean isEdge(Point p, Point q, int edgeThreshold) {
		return p.distance(q)<edgeThreshold;
	}
	private int degree(Point p, ArrayList<Point> points, int edgeThreshold) {
		int degree=-1;
		for (Point q: points) if (isEdge(p,q,edgeThreshold)) degree++;
		return degree;
	}
}
