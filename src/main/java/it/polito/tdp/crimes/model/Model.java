package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> vertici;
	
	public Model() {
		dao= new EventsDao();
	}
	
	public List<String> getCategoria(){
		return this.dao.getCategoria();
	}
	
	public List<Integer> getGiorno(){
		return this.dao.getGiorno();
	}
	
	public void creaGrafo(String categoria, Integer giorno) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		vertici= this.dao.getVertici(categoria, giorno);
		
		Graphs.addAllVertices(grafo, vertici);
		
		for(Adiacenze a: this.dao.getArchi(categoria, giorno)) {
			if(a.getV1()!=a.getV2() && this.grafo.containsVertex(a.getV1()) && this.grafo.containsVertex(a.getV2())) {
				Graphs.addEdgeWithVertices(grafo, a.getV1(), a.getV2(), a.getPeso());
			}
		}
		
		System.out.println("Grafo creato con "+ this.grafo.vertexSet().size()+" vertici e con "+ this.grafo.edgeSet().size()+" archi\n");
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenze> peso(){
		double pesoGrafo=0.0;
		double pesoMax=0.0;
		double pesoMin=0.0;
		
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>pesoMax) {
				pesoMax=this.grafo.getEdgeWeight(e);
			}
		}
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)<pesoMin) {
				pesoMin=this.grafo.getEdgeWeight(e);
			}
		}
		//peso grafo mediano
		pesoGrafo=(pesoMax+pesoMin)/2;
		
		List<Adiacenze> result= new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)<pesoGrafo) {
				result.add(new Adiacenze(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e)));
			}
		}
		System.out.println(pesoGrafo);
		return result;
		
	}
}
