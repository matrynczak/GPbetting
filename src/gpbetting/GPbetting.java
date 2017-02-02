/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gpbetting;

import static gpbetting.GPbetting.driver;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author mateusz
 */
public class GPbetting {

    static WebDriver driver;
    static WebDriver driver2;
    static Wait<WebDriver> wait;
    static String loginArea = "//input[@name='login']";
    static String passwordArea = "//input[@name='password']";
    static String login = "mateusz.rynczak";
    static String password = "ciastek90";
    static String submitLogin = "//button[@type='submit']";
    static String activeRound = "(//li[@data-ng-repeat='round in ctrl.rounds.running']/a)[1]";
    static String matches = "(//ul[@class='nav nav-second-level collapse in']/li/a)[1]";
    static String leftSideTeam = "(//td[@class='bet noselect right'])";
    static String rightSideTeam = "(//td[@class='bet noselect left'])";
    static String teamForm = "(//td[@class='form']/a)";
    static String searchBoxStats = "//input[@id='search-box']";
    static String searchBoxStatsToClick = "//form[@id='search-form']";
    static String goSearchingButton = "//div[@id='search-button']";
    static String resultOfTeamSearching = "(//div[@class='search-result']//td)[1]/a[contains(@href, 'Teams')]";
    static String saveBetButton = "//button[@class='btn btn-success btn-block btn-lg']";
    static Teams teams = new Teams();
    static ArrayList teamsFromGP = new ArrayList();
    static ArrayList teamsFromWS = new ArrayList();
    
    public static void main(String[] args) throws InterruptedException, IOException {
        teamsFromGP = Teams.createListWithTeamsNames("nazwyKlubow.txt");
        teamsFromWS = Teams.createListWithTeamsNames("nazwy.txt");
        
        System.setProperty("webdriver.gecko.driver","/home/mateusz/selenium-java-3.0.1/geckodriver");
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 30);
        driver.get("http://bettingleaguegp.appspot.com/");
        
	
        loginProcess();
                for(Integer i=1; i <= 23; i++){
                    String leftTeamName = getLeftTeamName(leftSideTeam, i);
                    String correctLeftTeam =  setCorrectTeamNameForSearching(leftTeamName, teamsFromGP, teamsFromWS);
                    ArrayList leftTeamFormTable = getFormTable(correctLeftTeam);
                    Integer formLeftTeamFormIndex = formIndex(leftTeamFormTable);
                    String rightTeamName = getRightTeamName(rightSideTeam, i);
                    String correctRightTeam =  setCorrectTeamNameForSearching(rightTeamName, teamsFromGP, teamsFromWS);
                    ArrayList rightTeamFormTable = getFormTable(correctRightTeam);
                    Integer formRightTeamFormIndex = formIndex(rightTeamFormTable);
                    Integer formDifferenceIndex = formDifference (formLeftTeamFormIndex, formRightTeamFormIndex);
                    System.out.print(formDifferenceIndex);
                    clickGoals(formDifferenceIndex, i, leftSideTeam, rightSideTeam);
                }
        driver.findElement(By.xpath(saveBetButton)).click();
        driver.quit();

	    }
    
    private static void loginProcess() throws InterruptedException {
        driver.findElement(By.xpath(loginArea)).click();
        driver.findElement(By.xpath(loginArea)).sendKeys(login);
        driver.findElement(By.xpath(passwordArea)).click();
        driver.findElement(By.xpath(passwordArea)).sendKeys(password);
        driver.findElement(By.xpath(submitLogin)).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath(activeRound)).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath(matches)).click(); 
    }
    
    private static String getLeftTeamName(String teamLeft, Integer i) throws InterruptedException{
        Thread.sleep(2000);
        String leftTeamName  = driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).getText();
        return leftTeamName;
    }
    
    private static String getRightTeamName(String teamRight, Integer i) throws InterruptedException{
        Thread.sleep(2000);
        String rightTeamName  = driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).getText();
        return rightTeamName;
    }
    
    private static ArrayList getFormTable(String teamLeft) throws InterruptedException{
        ArrayList formTable = new ArrayList();
        driver2 = new FirefoxDriver();
        driver2.get("https://www.whoscored.com/");
        driver2.findElement(By.xpath(searchBoxStats)).clear();
        driver2.findElement(By.xpath(searchBoxStatsToClick)).click();
        Thread.sleep(2000);
        driver2.findElement(By.xpath(searchBoxStats)).sendKeys(teamLeft);
        driver2.findElement(By.xpath(goSearchingButton)).click();
        Thread.sleep(2000);
        driver2.findElement(By.xpath(resultOfTeamSearching)).click();
        Thread.sleep(6000);
        Integer numberOfMatches = driver2.findElements(By.xpath(teamForm)).size();
        for(Integer i=1; i<=numberOfMatches; i++){
            String oneMatch = driver2.findElement(By.xpath(teamForm+"["+Integer.toString(i)+"]")).getText();
            formTable.add(oneMatch);
        }
        driver2.quit();
        return formTable;
    }
    
    
    private static Integer formIndex(ArrayList formList){
        int formPoint = 0;
        int listLength = formList.size();
                
        if (formList.get(listLength-1).equals("W")) formPoint += 10;
        if (formList.get(listLength-2).equals("W")) formPoint += 8;
        if (formList.get(listLength-3).equals("W")) formPoint += 6;
        
        if (formList.get(listLength-1).equals("D")) formPoint += 5;
        if (formList.get(listLength-2).equals("D")) formPoint += 3;
        if (formList.get(listLength-3).equals("D")) formPoint += 1;
        
        return formPoint;
    }
    
    private static Integer formDifference(Integer leftTeamFormIndex, Integer rightTeamFormIndex){
        Integer formDifferenceIndex = leftTeamFormIndex - rightTeamFormIndex;      
        return formDifferenceIndex;
    }
    
    private static void clickGoals(Integer formDifferencePoints, Integer i, 
            String teamLeft, String teamRight){
        driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
        
        if(formDifferencePoints >= 20) {
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
        }
        
        if(formDifferencePoints >= 15 && formDifferencePoints <20) {
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
        }
        
        if(formDifferencePoints > 10 && formDifferencePoints <15) {
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
        }
        
        if(formDifferencePoints > 5 && formDifferencePoints <=10) {
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
        }
        
        if(formDifferencePoints > 2 && formDifferencePoints <=5) {
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
        }
        
        if(formDifferencePoints <= -20) {
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
        }
        
        if(formDifferencePoints <= -15 && formDifferencePoints >-20) {
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
        }
        
        if(formDifferencePoints < -10 && formDifferencePoints >-15) {
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
        }
        
        if(formDifferencePoints < -5 && formDifferencePoints >= -10) {
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
        }
        
        if(formDifferencePoints < -2 && formDifferencePoints >= -5) {
            driver.findElement(By.xpath(teamLeft+"["+Integer.toString(i)+"]")).click();
            driver.findElement(By.xpath(teamRight+"["+Integer.toString(i)+"]")).click();
        }
        
        else;
    }
    
    private static String setCorrectTeamNameForSearching(String teamFromGPbetting, ArrayList GPteams, ArrayList WSteams){
        String correctNameForSearchingOnWhoScored = null;
        for(int i=0; i<GPteams.size(); i++){
            if(teamFromGPbetting.equals(GPteams.get(i).toString())){
                correctNameForSearchingOnWhoScored = WSteams.get(i).toString();
            }
        }
        return correctNameForSearchingOnWhoScored;
    }
}