package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFileChooser;

import puzzle.Crossword;
import puzzle.Puzzle;
import puzzle.PuzzleCell;
import puzzle.PuzzleWord;
import puzzle.WordSearch;
import puzzle.Puzzle.Direction;


/**
 * This class contains all of the File Input and Output methods.
 * 
 * @author Sad Panda Software
 * @version 1.0
 */
public class FileIO {
  
  /** A file chooser */
  private static JFileChooser chooser;
  
  /** The file that is to be loaded */
  private static File file;
  
  /** The array list of words */
  private static Vector <String> words = new Vector <String> ();
  
  /** Returns a list of words sorted by length (longest first)
   * @return ArrayList<String> - A list of words */
  public static Vector <String> getFile () {
    int status;
    status = getFileChooser().showOpenDialog (null);
    
    if (status == JFileChooser.APPROVE_OPTION) {
      file = getFileChooser().getSelectedFile ();
      System.out.println ("File selected to open: " + file.getName ());
      System.out.println ("Full path name: " + file.getAbsolutePath ());
    } else {
      words.clear ();
      return words;
    }
    
    try {
      getWords (file);
    } catch (IOException e) {
      System.out.println ("Error: IO Exception was thrown:" + e);
    }
    return words;
  }
  
  /** Processes a list of words from a file.
   * @param input a File
   * @throws IOException */
  private static void getWords (File input) throws IOException {
    FileReader fileReader = new FileReader (input);
    BufferedReader buffer = new BufferedReader (fileReader);
    String temp;
    
    while ((temp = buffer.readLine ()) != null)
      words.add (temp);
    buffer.close ();
  }
  
  /** Will call the appropriate save function based on whether or not a file has
   * been associated yet. If one has not, a new file will be created.
   * @param list Vector<String>
   * @throws IOException */
  public static void saveWords (ArrayList <String> list) {
    if (file != null) {
      try {
        save (list, file);
      } catch (IOException e) {
        System.out.println ("Error: IO Exception was thrown:" + e);
      }
    } else
      saveWordsAs (list);
  }
  
  /** Creates a new file and calls the save function to save the list of words to the new file.
   * @param list ArrayList<String>
   * @throws IOException */
  private static void saveWordsAs (ArrayList <String> list) {
    int status;
    File newFile = new File ("empty");
    status = getFileChooser().showSaveDialog (null);
    
    if (status == JFileChooser.APPROVE_OPTION) {
      newFile = getFileChooser().getSelectedFile ();
      System.out.println ("File chosen to save to: " + newFile.getName ());
      System.out.println ("Full path to file: " + newFile.getAbsolutePath ());
      try {
        save (list, newFile);
      } catch (IOException e) {
        System.out.println ("Error: IO Exception was thrown:" + e);
      }
    }
  }
  
  /** Will perform the actual save of the word list to the location provided.
   * @param list ArrayList<String>
   * @param location Location of file
   * @throws IOException */
  private static void save (ArrayList <String> list, File location) throws IOException {
    BufferedWriter buffer = new BufferedWriter (new FileWriter (location));
    for (String word : list) {
      buffer.write (word + "\n");
    }
    buffer.close ();
  }
  
  private static JFileChooser getFileChooser() {
    if (chooser == null) {
      chooser = new JFileChooser();
    }
    return (chooser);
  }
  
  public static void exportHTML (Puzzle puzzle) {
    
  }
  
  public static void savePuzzle (Puzzle puzzle) {
    int status;
    File newFile = new File ("empty");
    status = getFileChooser().showSaveDialog (null);
    
    if (status == JFileChooser.APPROVE_OPTION) {
      newFile = getFileChooser().getSelectedFile ();
      System.out.println ("File chosen to save to: " + newFile.getName ());
      System.out.println ("Full path to file: " + newFile.getAbsolutePath ());
      if(puzzle instanceof WordSearch)
      {
        try {
          saveWordSearch (puzzle, newFile);
        } catch (IOException e) {
          System.out.println ("Error: IO Exception was thrown:" + e);
        }
      }
      else if(puzzle instanceof Crossword)
      {
        try {
          saveCrossword (puzzle, newFile);
        } catch (IOException e) {
          System.out.println ("Error: IO Exception was thrown:" + e);
        }
      }
      else
      {
        System.out.println("No Puzzle to save.");
      }
    }
  }
  
  private static void saveWordSearch (Puzzle puzzle, File location) throws IOException {
    BufferedWriter buffer = new BufferedWriter (new FileWriter (location));
    ArrayList <PuzzleWord> list = puzzle.getWordList ();
    PuzzleCell[][] matrix = puzzle.getMatrix ();
    buffer.write ("wordsearch\n");
    buffer.write (puzzle.getNumWords () + "\n");
    buffer.write (puzzle.getMatrixHeight () + "\n");
    buffer.write (puzzle.getMatrixWidth () + "\n");
    for (PuzzleWord word : list) {
      buffer.write (word.getWord() + " " + word.getRow () + " " + word.getColumn() + " " + word.getDirection ().ordinal () + "\n");
    }
    String s = "";
    for (int r = 0; r < matrix.length; r++) {
      for (int c = 0; c < matrix[0].length; c++) {
        s += matrix[r][c] + " " + matrix[r][c].getNumWords() + " ";
      }
      s += "\n";
    }
    buffer.write (s);
    buffer.close ();
  }
  
  private static void saveCrossword (Puzzle puzzle, File location) throws IOException {
    BufferedWriter buffer = new BufferedWriter (new FileWriter (location));
    ArrayList <PuzzleWord> list = puzzle.getWordList ();
    buffer.write ("crossword\n");
    buffer.write (puzzle.getNumWords () + "\n");
    buffer.write (puzzle.getMatrixHeight () + "\n");
    buffer.write (puzzle.getMatrixWidth () + "\n");
    for (PuzzleWord word : list) {
      buffer.write (word.getWord() + " " + word.getRow () + " " + word.getColumn() + " " + word.getDirection ().ordinal () + "\n");
    }
    buffer.close ();
  }

  public static Puzzle loadPuzzle () {
    int status;
    status = getFileChooser().showOpenDialog (null);
    Puzzle puzzle = null;
    Scanner scan = null;
    String type = "";
    
    if (status == JFileChooser.APPROVE_OPTION) {
      file = getFileChooser().getSelectedFile ();
      System.out.println ("File selected to open: " + file.getName ());
      System.out.println ("Full path name: " + file.getAbsolutePath ());
    } else {
      return puzzle;
    }
    try{
      scan = new Scanner(file);
      type = scan.nextLine();
    }
    catch (FileNotFoundException e){
      System.out.println ("Error: File Not Found Exception was thrown:" + e);
    }
    
    if(type.equals ("wordsearch"))
      {
        puzzle = loadWordSearch (scan);
        return puzzle;
      }
    else if(type.equals ("crossword"))
    {
        puzzle = loadCrossword (scan);
        return puzzle;
    }
    else
    {
      System.out.println("Invalid File");
      return puzzle;
    }
  }
  
  private static Puzzle loadWordSearch(Scanner scan)
  {
    Puzzle puzzle = new WordSearch();
    PuzzleWord word = new PuzzleWord();
    PuzzleCell cell = new PuzzleCell();
    int height, width;
    Scanner scan2 = new Scanner(scan.nextLine());
    puzzle.setNumWords (scan2.nextInt ());
    scan2 = new Scanner(scan.nextLine());
    height = scan2.nextInt ();
    puzzle.setMatrixHeight(height);
    scan2 = new Scanner(scan.nextLine());
    width = scan2.nextInt ();
    ArrayList<PuzzleWord> words = new ArrayList<PuzzleWord>();
    for(int i = 0; i < puzzle.getNumWords(); i++)
    {
      scan2 = new Scanner(scan.nextLine());
      word.setWord (scan2.next ());
      word.setRow (scan2.nextInt ());
      word.setColumn (scan2.nextInt ());
      word.setDirection (Direction.values()[scan2.nextInt()]);
      words.add (word);
    }
    puzzle.setWordList(words);
    PuzzleCell[][] matrix = new PuzzleCell[height][width];
    for (int r = 0; r < matrix.length; r++) {
      scan2 = new Scanner(scan.nextLine());
      for (int c = 0; c < matrix[0].length; c++) {
        cell.setCharacter (scan2.next ().charAt (0));
        cell.setNumWords(scan2.nextInt ());
      }
    }
    return puzzle;
  }
  
  private static Puzzle loadCrossword(Scanner scan)
  {
    Puzzle puzzle = new WordSearch();
    PuzzleWord word = new PuzzleWord();
    int height, width;
    Scanner scan2 = new Scanner(scan.nextLine());
    puzzle.setNumWords (scan2.nextInt ());
    scan2 = new Scanner(scan.nextLine());
    height = scan2.nextInt ();
    puzzle.setMatrixHeight(height);
    scan2 = new Scanner(scan.nextLine());
    width = scan2.nextInt ();
    puzzle.setMatrixWidth (width);
    ArrayList<PuzzleWord> words = new ArrayList<PuzzleWord>();
    for(int i = 0; i < puzzle.getNumWords(); i++)
    {
      scan2 = new Scanner(scan.nextLine());
      word.setWord (scan2.next ());
      word.setRow (scan2.nextInt ());
      word.setColumn (scan2.nextInt ());
      word.setDirection (Direction.values()[scan2.nextInt()]);
      words.add (word);
    }
    puzzle.setWordList(words);
    return puzzle;
  }
}
