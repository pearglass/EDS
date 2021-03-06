package unbbayes.prs.bn;

import java.util.Collection;
import java.util.List;

import unbbayes.prs.INode;

/**
 * This interface represents a junction tree for BN.
 * @see JunctionTree for default implementation
 * @author Shou Matsumoto
 *
 */
public interface IJunctionTree {

	/**
	 * @return estimated total probability
	 */
	public abstract float getN();

	/**
	 * Stores the separator in junction three
	 * @param sep
	 */
	public abstract void addSeparator(Separator sep);
	
	/**
	 * Deletes a separator from junction tree and clears all indexes
	 * @param sep
	 */
	public abstract void removeSeparator(Separator sep);
	
	/**
	 * This method returns cliques containing all specified nodes
	 * @param nodes : only cliques containing these nodes will be returned.
	 * @param maxCount : no more than this quantity of cliques will be returned.
	 * @return list of cliques
	 */
	public List<Clique> getCliquesContainingAllNodes(Collection<INode> nodes, int maxCount);

	/**
	 * This method returns separators containing all specified nodes
	 * @param nodes : only cliques containing these nodes will be returned.
	 * @param maxCount : no more than this quantity of cliques will be returned.
	 * @return list of separators
	 */
	public List<Separator> getSeparatorsContainingAllNodes(Collection<INode> nodes, int maxCount);
	
//	/**
//	 * The current size of the collection of separators stored in this
//	 * junction tree.
//	 * @return
//	 */
//	public abstract int getSeparatorsSize();

//	/**
//	 * Obtains the separator in a given index.
//	 * @param index
//	 * @return
//	 */
//	public abstract Separator getSeparatorAt(int index);
	
	/**
	 *@return    list with associated cliques
	 */
	public abstract List<Clique> getCliques();
	
	/**
	 * Deletes a clique from this junction tree
	 * @param cliques : list of cliques to delete
	 * @param isToUpdateIndexes : if true, any internal indexes must be updated. False otherwise.
	 * @return true if this method has resulted in modification. False otherwise.
	 */
	public abstract boolean removeCliques(Collection<Clique> cliques, boolean isToUpdateIndexes);
	
	/**
	 * This method should be equivalent to {@link #removeCliques(Collection, boolean)} with true as the second argument.
	 * @deprecated use {@link #removeCliques(Collection, boolean)} instead
	 */
	public abstract boolean removeCliques(Collection<Clique> cliques);
	
	/**
	 * @return list with associated separators
	 */
	public abstract Collection<Separator> getSeparators();

	/**
	 *  Verifies global consistency.
	 *  It applies the "collect" (propagate messages from leaves to root clique) and then "distribute" (propagate
	 *  messages from root clique to leaves).
	 *  This is equivalent to calling {@link #consistency(Clique)} to
	 *  the root clique (assuming that we can visit all cliques from the root)
	 */
	public abstract void consistency() throws Exception;
	
	/**
	 * Verifies global consistency.
	 *  It applies the "collect" (propagate messages from leaves to root clique) and then "distribute" (propagate
	 *  messages from root clique to leaves). However, its scope is limited only to cliques below (i.e. descendants)
	 *  of the clique passed as its argument.
	 *  @param isToContinueOnEmptySep : if false, propagation will not recursively
	 * guarantee global consistency to subtrees connected with empty separators.
	 * If evidences are expected to be across several cliques, set this to true,
	 * otherwise, if evidences are expected to be present only at the subtree
	 * of rootClique, then set this as false.
	 * @throws Exception
	 * @see unbbayes.prs.bn.IJunctionTree#consistency()
	 */
	public void consistency(Clique rootClique, boolean isToContinueOnEmptySep) throws Exception;

	/**
	 *  Initialize belief
	 */
	public abstract void initBeliefs() throws Exception;

	/**
	 * Returns the Separator associated with these Cliques, assuming no orientation.
	 *
	 * @param clique1 Clique 1
	 * @param clique2 Clique 2
	 * @return The separator associated with these Cliques or null if this separator doesn't exist.
	 */
	public abstract Separator getSeparator(Clique clique1, Clique clique2);
	
	/**
	 * Initializes the probability potential of a random variable. Particularly, it
	 * initializes potentials of either a {@link Separator} or a {@link Clique}
	 * @param rv
	 */
	public void initBelief(IRandomVariable rv);
	
	/**
	 * This method can be used to obtain a clique containing most of the nodes passed in its argument.
	 * @param nodes: nodes that this method will use in order to attempt to find
	 * a clique containing most of them.
	 * @return a clique which may contain all the nodes. If it does not
	 * contain all the nodes, it will contain nodes whose the size of the intersection with
	 * the passed argument is as large as possible.
	 * It will return null if no node or no network or no junction tree was specified, or when the junction tree does not contain the specified node.
	 */
	public List<Clique> getCliquesContainingMostOfNodes(Collection<INode> nodes);
	
	/**
	 * @return : the total quantity of separators in this junction tre.
	 * @deprecated you can use {@link #getSeparators()} and thenm {@link Collection#size()} instead.
	 */
	public int getSeparatorsSize();
	
	/**
	 * @param index : an index for the separator
	 * @return the separator identified by the index.
	 * @deprecated separators are not identified by indexes anymore. They can be retrieved from what cliques or nodes do use them.
	 * In case you need to iterate over them, use {@link #getSeparators()} instead.
	 * @see #getSeparator(Clique, Clique)
	 * @see #getSeparatorsContainingAllNodes(Collection, int)
	 */
	public Separator getSeparatorAt(int index);
	
	/**
	 * Implementations should search for cliques containing the specified nodes (not necessarily simultaneously), and
	 * return all cliques that can have a path to such clique with no empty separators in the path.
	 * @param nodes : nodes to be considered
	 * @param cliquesToIgnore : cliques contained in this collection will not be considered in the search.
	 * Implementations may choose to stop search when these cliques are found.
	 * @return : cliques containing the specified nodes (not necessarily simultaneously), and
	 * all cliques that can have a path to such clique with no empty separators in the path.
	 */
	public Collection<Clique> getCliquesConnectedToNodes(Collection<INode> nodes, Collection<Clique> cliquesToIgnore);
	
	/**
	 * Checks whether the first clique is an predecessor of the other
	 * @param predecessorToTest : the method will whether this clique is a predecessor of the other
	 * @param clique : predecessor of this clique will be retrieved for the check.
	 * @return : true if predecessorToTest is a predecessor of clique. False otherwise
	 * @see Clique#getParent()
	 */
	public boolean isPredecessor(Clique predecessorToTest, Clique clique);
	
	
	/**
	 * @return true if the implementation is using some sort of approximation (e.g. approximate structure). 
	 * False otherwise.
	 */
	public boolean isUsingApproximation();
	
	/**
     * Finds the shortest path between two cliques.
     * @param from : a clique to start from. This needs to be a clique in this junction tree.
     * @param to : a clique to finish search. This also needs to be in this junction tree.
     * @return : a list of sequence of cliques that forms a path between the two cliques in this junction tree.
     * The clique "from" must be the 1st element, and the clique "to" must be the last element in the returned list
     * (please, notice that if "to" == "from", then implementations may have either 1 or 2 elements in the returned list).
     */
    public List<Clique> getPath(Clique from, Clique to);

}