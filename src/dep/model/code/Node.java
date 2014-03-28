package dep.model.code;


public class Node
{
	
	int data;
	Node next;
	Node prev;
	
	public Node(int data)
	{
		this.data = data;
	}
	
	void insertNext(Node node)
	{
		if(this.next==null)
			this.next = node;
		else
		{
			node.next = this.next;
			this.next = node;
		}
	}
	
	Node deleteNext()
	{
		Node temp = this.next;
		if(this.next==null)
			return null;
		else
		{
			this.next =this.next.next;		
			return temp;
		}
	}
}
