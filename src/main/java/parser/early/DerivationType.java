/**
 * 
 */
package parser.early;

/**
 * 
 * @author Fabian Gallenkamp
 */
public enum DerivationType {
	Consume, 
	PredictTraversation, 
	PredictSubstitution, 
	PredictLeftAux, PredictRightAux,
	CompleteTraversation,
	CompleteSubstitution, 
	CompleteLeftAdjunction,CompleteRightAdjunction;
}
