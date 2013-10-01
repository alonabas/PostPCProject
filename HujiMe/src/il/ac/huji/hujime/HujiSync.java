package il.ac.huji.hujime;

import il.ac.huji.hujime.Lessons.Semester;
import android.content.Context;

public class HujiSync {
	private Context _context;
	public interface Sync{
    	boolean sync();
    }
	public HujiSync(Context context){
		_context=context;
	}
	public Sync gradesSync = new Sync(){
		@Override
		public boolean sync() {
			Grades grades = new Grades(_context);
			if (!grades.getGrades()) {
				return false;
			}
			grades.addToDb();
			return true;
		}
	};
	public Sync examsSync = new Sync(){
		@Override
		public boolean sync() {
			Exams exams = new Exams(_context);
			if(!exams.getExams()){
				return false;
			}
			exams.addToDb();
			return true;
		}
	};
	public Sync lessonsSync = new Sync(){
		@Override
		public boolean sync() {
			Lessons lessons_A = new Lessons(_context);
			lessons_A.getDates();
			lessons_A.getDates();
			if(!lessons_A.getLessons(Semester.A)){
				return false;
			}
			lessons_A.addToDb(Semester.A);
			Lessons lessons_B = new Lessons(_context);
			if(!lessons_B.getLessons(Semester.B)){
				return false;
			}
			lessons_B.addToDb(Semester.B);
			return true;
		}
	};
}	
	