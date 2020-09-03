package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	
	private ImdbDAO dao;
	private Map<Integer,Actor>idMapActor;
	private Graph<Actor,DefaultWeightedEdge>grafo;
	
	
	
	public Model() {
		dao= new ImdbDAO();
		idMapActor= new HashMap<Integer, Actor>();
		dao.MAPlistAllActors(idMapActor);
	}
	
	
	public List<String> getGenere(){
		return dao.getGenere();
	}
	
	
	public void creaGrafo(String genereSelezionato) {
		
		grafo= new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getAttoreVertice(genereSelezionato, idMapActor));
		
		for(Adiacenza ad : dao.getAdiacenzaArtisti(genereSelezionato, idMapActor)) {
			if(grafo.containsVertex(ad.getA1())&&grafo.containsVertex(ad.getA2())) {
				Graphs.addEdgeWithVertices(grafo, ad.getA1(), ad.getA2(),ad.getPeso());
			}
		}
		
		
		
		
		
		
		
	}
	
	
	
	
	
	public int getNVertici() {
		
		return this.grafo.vertexSet().size();
	}
	
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	
	
	public List<Actor>getAttoriDelGrafo(){
		List<Actor>result= new ArrayList<Actor>(this.grafo.vertexSet());
		return result;
		
		
	}
	
	
	
	
	
	public List<Actor>getAttoriSimili(Actor attoreSelezionato){
		
		List<Actor>visita= new ArrayList<Actor>();
		GraphIterator<Actor, DefaultWeightedEdge>dfv= new DepthFirstIterator<Actor, DefaultWeightedEdge>(this.grafo,attoreSelezionato);
		while(dfv.hasNext()) {
			visita.add(dfv.next());
		}
		Collections.sort(visita);
		return visita;
		
		
		
		
		
	}
	
	
	

}
