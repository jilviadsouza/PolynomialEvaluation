package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 * Jilvia D'Souza
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		Node curr = new Node(-100, -20777777, null); // curr is a new node with random limits 
		Node head = curr; // head is a new node 
		while(poly1 != null || poly2 != null) { // while poly1 OR poly2 has things in them
			curr.next = new Node(0, 0, null); // the node AFTER head is a new node 
			curr = curr.next; // curr is updated
      if(poly1 != null && poly2 != null){ // if both poly1 and poly2 is NOT empty
      	if(poly1.term.degree < poly2.term.degree){ // starting from the lowest, if the degree is less than the other
        	curr.term.degree = poly1.term.degree; // curr'a degree is updated to poly1's, poly1's lowest degree term is pushed into the empty node (curr)
          curr.term.coeff = poly1.term.coeff; // along with their coeffs
          poly1 = poly1.next; // poly1 is updated to its next node and the loop is repeated
        }
        else if(poly2.term.degree<poly1.term.degree){ // if poly2's degree is less than poly1's degree
        	curr.term.degree=poly2.term.degree; // push poly2's degree into curr's node
          curr.term.coeff=poly2.term.coeff; // coeff also gets pushed
          poly2=poly2.next; // poly2 is updated
        }
        else{ 										// if neither of them occurs (is the degrees are equal)
        	curr.term.degree = poly1.term.degree;  // push either poly1 or poly2's degree into curr (here we chose poly1)
          curr.term.coeff = poly1.term.coeff + poly2.term.coeff; //
          poly1 = poly1.next;
          poly2 = poly2.next;
        }
      }
      else if(poly1!=null){
      	curr.term.degree=poly1.term.degree;
        curr.term.coeff=poly1.term.coeff;
        poly1=poly1.next;
      }
      else{
      	curr.term.degree=poly2.term.degree;
        curr.term.coeff=poly2.term.coeff;
        poly2=poly2.next;
      }
		}
		return removeZeroElements(head);
	}
  
  public static Node removeZeroElements(Node node){
  	Node head=node.next;
  	Node prev=node;
  	
    while(head!=null && head.term.coeff==0){
    	prev=head;
    	head=head.next;
    }
    
    Node curr=head;
    
    while(curr != null){
    	if(curr.term.coeff==0) {
    		prev.next=curr.next;
    		curr=curr.next;
    	}
    	else {
    		prev=prev.next;
    		curr=curr.next;
    	}
    }
    
    if(prev!=null && prev.term.coeff==0) prev=null;
  
  	return head;
  }
  
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	

  public static Node multiply(Node poly1, Node poly2) {
		if(poly1 == null || poly2==null) return null;
		
		Node poly1Curr = poly1;
		Node poly2Curr = poly2;

		Node result=new Node(0, -20777777, null);

		while(poly1Curr!=null){
			poly2Curr=poly2;
			while(poly2Curr!=null){
				float coeff=poly1Curr.term.coeff*poly2Curr.term.coeff;
				int degree=poly1Curr.term.degree+poly2Curr.term.degree;
				Node temp=new Node(coeff, degree, null);
				result=insertIntoResult(result, temp);
				poly2Curr=poly2Curr.next;
			}
			poly1Curr=poly1Curr.next;
		}
		
		return removeZeroElements(result);
	}

	public static Node insertIntoResult(Node result, Node temp){
		if(result == null) return temp;
		
		Node head=null;
		if(temp.term.degree<result.term.degree) head=temp;
		else head = result;
		
		Node prev=null;
		while(result!=null){
			if(result.term.degree>=temp.term.degree) break;
			prev=result;
			result=result.next;
		}
		if (result == null) prev.next = temp;
		else if(temp.term.degree==result.term.degree) result.term.coeff+=temp.term.coeff;
		else if(result.term.degree>temp.term.degree) {
			temp.next=result;
			if(prev!=null) prev.next=temp;
		}
		else {
			result.next=temp;
		}

		return head;
	}

	
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		float answer = 0;
		while(poly != null) {
			answer += poly.term.coeff * (Math.pow(x, poly.term.degree));
			poly = poly.next;
		}
		return answer;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
	
	
	
}
