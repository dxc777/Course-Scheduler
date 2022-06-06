package DS;

import java.util.Arrays;

public class AdjMatrix implements Graph
{
	private int[][] matrix;

	private int[] nodeValues;
	
	public AdjMatrix(int size) 
	{
		matrix = new int[size][size];
		nodeValues =new int[size];
	}

	@Override
	public int nodeCount()
	{
		return matrix.length;
	}

	@Override
	public void addEdge(int vertex, int adjVertex, int weight)
	{
		if(weight == 0)return;
		matrix[vertex][adjVertex] = weight;
	}

	@Override
	public void removeEdge(int vertex, int adjVertex)
	{
		matrix[vertex][adjVertex] = 0;
	}

	@Override
	public boolean hasEdge(int vertex, int adjVertex)
	{
		return matrix[vertex][adjVertex] != 0;
	}

	@Override
	public int weight(int vertex, int adjVertex)
	{
		return matrix[vertex][adjVertex];
	}

	@Override
	public void setValue(int vertex, int value)
	{
		nodeValues[vertex] = value;
	}

	@Override
	public int getValue(int vertex)
	{
		return nodeValues[vertex];
	}

	@Override
	public void fillValues(int value)
	{
		Arrays.fill(nodeValues, value);
	}

	@Override
	public int[] neighbors(int vertex)
	{
		int size = 0;
		for(int i = 0; i <  matrix[vertex].length; i++) 
		{
			if(matrix[vertex][i] != 0) 
			{
				size++;
			}
		}
		
		int[] adjVertexes = new int[size];
		int index = 0;
		
		for(int i = 0; i < matrix[vertex].length; i++)
		{
			if(matrix[vertex][i] != 0) 
			{
				adjVertexes[index++] = i;
			}
		}
		return adjVertexes;
	}
	
	
}
