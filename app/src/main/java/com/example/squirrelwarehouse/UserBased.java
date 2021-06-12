package com.example.squirrelwarehouse;// package algorithm;

public class UserBased {
	
	String user;	// 현재유저
	int userIndex; 	// 현재 유저의 인덱스
	int[][] data = {{3,5,0,1,1,2,0,0,1,0},
					{3,1,0,5,0,0,1,2,4,0},
					{1,3,4,0,1,2,4,0,0,5},
					{1,0,3,0,3,0,4,2,0,3}};
	double[][] sim;
	String[] users = {"sewon","yeeun","yesol","eunbae"};
	
	public UserBased(String user) {
		this.user = user;
		getData();
		
		// 유사도 행렬
		for(int i=0; i<data.length-1; i++) {
			for(int j=i+1; j<data.length; j++) {
				sim[i][j] = sim[j][i] = cosineSimilarity(data[i],data[j]);
			}
		}
		
		// 유사도 행렬 출력
		for(int i=0; i<data.length; i++) {
			for(int j=0; j<data.length; j++) {
				System.out.print(sim[i][j]+" ");
			}
			
			System.out.println();
		}
		
		
		// 현재 유저와 다른 유저와의 유사도만 뽑아내기
		int i;
		for(i=0; i<users.length; i++) {
			if(users[i].equals(user))
				//userIndex = i;
				break;
		}
		
		for(double dd : sim[i])
			System.out.print(dd+ " ");
		
		
		// 유사도 제일 높은 사람의 정보 출력하기
		double max = sim[i][0];
		int index = 0;
		for(int j=0; j<users.length; j++) {
			if(max < sim[i][j]) {
				max = sim[i][j];
				index = j;
			}
		}
		
		System.out.println("\n"+ index +" " + users[index]);
		
		// 나랑 유사도가 가장 높은 사람의 물건 선호도 출력
		for(int dd : data[index]) {
			System.out.print(dd + " ");
		}
		System.out.println();
		
		// 나의 물건 선호도 출력
		for(int dd : data[i]) {
			System.out.print(dd + " ");
		}
		
		System.out.println();
		
		// 내가 보지 않았지만, 상대는 관심있는 물건, 인덱스 출력
		for(int j=0; j<data[i].length; j++) {
			if(data[i][j]==0 && data[index][j]!=0) {
				System.out.print(j + " ");
			}
		}
		
		
		
	}
	
	private void getData() {
		// 파이어베이스에서 데이터 불러와야 함
		// 지금은 일단 더미 데이터로 해보기
		// favorite 데이터랑 user 데이터 가져와야 함
		// data, users
		
		sim = new double[data.length][data.length];
		
	}
	
	private double cosineSimilarity(int[] user1, int[] user2) {
		// 코사인 유사도 계산
		
		double sum = 0;
		double sum1 = 0;
		double sum2 = 0;
		
		for(int i=0; i<user1.length; i++) {
			sum += user1[i] * user2[i];
			sum1 += Math.pow(user1[i], 2);
			sum2 += Math.pow(user2[i], 2);
		}
		
		
		double similarity = sum / (Math.sqrt(sum1) * Math.sqrt(sum2));
		
		return similarity;
		
	}
	
	

	public static void main(String[] args) {
		UserBased userbased = new UserBased("yeeun");
		

	}

}
