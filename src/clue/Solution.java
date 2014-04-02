package clue;

public class Solution {

	public String person,weapon,room;
	
	public Solution (String person, String weapon, String room) {
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Solution)) return false;
		Solution s = (Solution) obj;
		if (this.person.equals(s.person) && this.weapon.equals(s.weapon) && this.room.equals(s.room)) return true;
		else return false;
	}

	@Override
	public String toString() {
		return "Solution [person=" + person + ", weapon=" + weapon + ", room="
				+ room + "]";
	}
	
}
