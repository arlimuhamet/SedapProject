
# we read information from text file
with open('data.txt', 'r', encoding='utf-8') as f:
    lines = f.readlines()

# create list with data from text file
listOfStudents = eval(lines[0].strip())

listOfCourses = eval(lines[1].strip())

listOfLectures = eval(lines[2].strip())

admin = eval(lines[3].strip())

listOfRegistrars = eval(lines[4].strip())

listOfAccountant = eval(lines[5].strip())


def updateListInTextFile():
    #we update text file info
    with open('data.txt', 'w', encoding='utf-8') as f:
        f.write(str(listOfStudents) + '\n')
        f.write(str(listOfCourses) + '\n')
        f.write(str(listOfLectures) + '\n')
        f.write(str(admin) + '\n')
        f.write(str(listOfRegistrars) + '\n')
        f.write(str(listOfAccountant) + '\n')


################################################################################################### START BLOCK

def start():
        print('Hello user!')
        choice = input('Choose your position:\n1. Accountant \n2. Student \n3. Registrer \n4. Lecturer \n5. Admin \n6. Exit \nPlease type a number of your choice:')
        if choice == '1':
            check = login_check()
            if check in listOfAccountant:
                accountant()
            else:
                print('Login error, check you login or password details')
                return start()
        elif choice == '2':
            loginStudent()
        elif choice == '3':
            check = login_check()
            if check in listOfRegistrars:
                Register()
            else:
                print('Login error, check you login or password details')
                return start()
        elif choice == '4':
            lecturerFunctionalitiesEntryPoint()
        elif choice == '5':
            check = login_check()
            if check in admin:
                Administartor()
            else:
                print('Login error, check you login or password details')
                return start()
        elif choice == '6':
            print('Goodbye!')
        else:
            print('Please try again, choose the number of your choice')
            return start()


#################################################################################################### STUDENT

def viewAllAvailableModules(studentId):
    #indexOfModule - variable, which we use to give number in order during printing process of all available modules
    indexOfModule = 1
    #listOfModules - list, which will contain all modules name
    listOfModules = []
    print('All available modules:')
    #we use loop print all available modules with order number. Also, we add each module to listOfModules to return it after
    for item in listOfCourses:
        listOfModules.append(item[0])
        print(indexOfModule, ')', item[0])
        indexOfModule = indexOfModule+1
    return Student(studentId)




def viewOwnGrades(studentId, typeOfUser):
    #myGrades - list, where will be all grades of modules, which student with this studentId takes
    myGrades = []
    
    #convert studentId to int number, because we will use it as index later
    intStudentId = int(studentId)
    
    #student - list, where contains all info about student with this studentId
    student = listOfStudents[intStudentId-1]

    #listOfMyModules - list, where contains all modules that student with this studentId takes
    listOfMyModules = student[len(student)-2]

    #we check if the student have any modules to avoid useless actions with empty list
    
    if(len(listOfMyModules)!=0):
        #we go through the list with all available modules. There is if statemnt inside: If student with this studentId takes module, we start checking the list of students of this module and add the list with subjectName and grade into list of all grades. After all operation, we print all grades 
        for i in range(len(listOfCourses)):
    
            #course - list, where 1 element - subject name, 2 element - list of students for course
            course = listOfCourses[i]
            subjectName = course[0]
            listOfStudentsForSubject = course[1]
        
            if(subjectName in listOfMyModules):
                for i in range(len(listOfStudentsForSubject)):
                    #studentInCourse - current student list, where 1 element - studentId, 2 - studentGrade, 3 - studentAttendance
                    studentInCourse = listOfStudentsForSubject[i]
                    studentIdInCourse = studentInCourse[0]
                    studentGradeInCourse = studentInCourse[1]
                    if(studentIdInCourse==studentId):
                        myGrades.append([subjectName, studentGradeInCourse])
        print("Your grades:")
        print(myGrades)
        if(typeOfUser=='Student'):
            return Student(studentId)
        elif(typeOfUser=='Register'):
            return Register()
    else:
        print('Student didnt start any modules')
        if(typeOfUser=='Student'):
            return Student(studentId)
        elif(typeOfUser=='Register'):
            return Register()




def findAverageAttendance(listOfAttendance):
    averageAttendance = 0
    quantityOfSubjects = 0
    #we go through listOfAttendance, where each element - list, where 1 element - subject name, 2 element - attendance percentage. 
    for item in listOfAttendance:
        attendance = item[1]
        #we dont count NAN attendance
        if(attendance=='NAN'):
            continue
        quantityOfSubjects = quantityOfSubjects+1
        #after we got attendance value, we remove sign "%" from the end and convert to float. If it was "100%", it will be 100.00
        attendance = float(attendance[:-1])
        #after all iteration we will find sum of all attendance
        averageAttendance = averageAttendance+attendance
    #find average attendance and return it
    if(len(listOfAttendance)!=0):
        return averageAttendance/quantityOfSubjects
    return 'You didnt attend any subjects yet'


def viewAttendance(studentId):
    
    myAttendance = []
    intStudentId = int(studentId)
    #student - list, where contains all info about student with this studentId
    student = listOfStudents[intStudentId-1]

    #listOfMyModules - list, where contains all modules that student with this studentId takes
    listOfMyModules = student[len(student)-2]
    
    #we check if the student have any modules to avoid useless actions with empty list
    
    if(len(listOfMyModules)!=0):
        #we go through the list with all available modules. There is if statemnt inside: If student with this studentId takes module, we start checking the list of students of this module and add the list with subjectName and attendance into list of all attendance. After all operation, we print all attendance and average
        for i in range(len(listOfCourses)):
            #course - list, where 1 element - subject name, 2 element - list of students for course
            course = listOfCourses[i]
            subjectName = course[0]
            listOfStudentsForSubject = course[1]
        
            if(subjectName in listOfMyModules):
                for i in range(len(listOfStudentsForSubject)):
                    #studentInCourse - current student list, where 1 element - studentId, 2 - studentGrade, 3 - studentAttendance
                    studentInCourse = listOfStudentsForSubject[i]
                    studentIdInCourse = studentInCourse[0]
                    studentAttendanceInCourse = studentInCourse[2]
                    if(studentIdInCourse==studentId):
                        myAttendance.append([subjectName, studentAttendanceInCourse])
    
        averageAttendance = findAverageAttendance(myAttendance)
        print('Your attendance:')
        print(myAttendance)
        print('Average attendance:', averageAttendance)
        return Student(studentId)
    else:
        print('Student didnt start any modules')
        return Student(studentId)
    

def getListOfAllAvailableModules():
    #indexOfModule - variable, which we use to give number in order during printing process of all available modules
    indexOfModule = 1
    #listOfModules - list, which will contain all modules name
    listOfModules = []
    print('All available modules:')
    #we use loop print all available modules with order number. Also, we add each module to listOfModules to return it after
    for item in listOfCourses:
        listOfModules.append(item[0])
        print(indexOfModule, ')', item[0])
        indexOfModule = indexOfModule+1
    return listOfModules


def enrolInModule(studentId, typeOfUser):
    #convert studentId to int number, because we will use it as index later
    intStudentId = int(studentId)
    
    #student - list, where contains all info about student with this studentId
    student = listOfStudents[intStudentId-1]

    #listOfMyModules - list, where contains all modules that student with this studentId takes
    listOfMyModules = student[len(student)-2]
    
    #listOfModules - list, where will be contained all modules
    listOfModules = getListOfAllAvailableModules()

    studentModuleChoice = input('Pick a number what module do you want to choose')
    if len(listOfMyModules)==len(listOfModules):
        print('You have already taken all courses')
        if(typeOfUser=='Student'):
            return Student(studentId)
        elif(typeOfUser=='Register'):
            return Register()
    #isdigit() check if the string input that have all characters in it as a digits. Also we check here so user would choose number in given range, and not give the chance to choose module that was already taken by student
    elif(studentModuleChoice.isdigit() and int(studentModuleChoice)>0 and int(studentModuleChoice)<=len(listOfModules) and listOfModules[int(studentModuleChoice)-1] not in listOfMyModules):
        #we add subject to students modules list
        listOfMyModules.append(listOfModules[int(studentModuleChoice)-1])
        
        #we find with the loop list by name and add info about student to list of subject
        for i in range(len(listOfCourses)):
            if(listOfCourses[i][0]==listOfModules[int(studentModuleChoice)-1]):
                #course - list, where 1 element - subject name, 2 element - list of students for course
                course = listOfCourses[i]
                listOfStudentsForSubject = course[1]
                #we add to list of students new student, but for attendance and grade put NAN, because student hasnt attended classes yet
                listOfStudentsForSubject.append([studentId, 'NAN', 'NAN'])
                #replace the old version with updated
                course[1] = listOfStudentsForSubject
                listOfCourses[i] = course
                break
        updateListInTextFile()
        print('You have successfully added course to your profile!')
        print(listOfStudents[intStudentId-1])
        if(typeOfUser=='Student'):
            return Student(studentId)
        elif(typeOfUser=='Register'):
            return Register()

    else:
        print('Incorrect input or you have already chosen this course. Type one of the given numbers')
        return enrolInModule(studentId, typeOfUser)


def unenrollInModule(studentId):
    #convert studentId to int number, because we will use it as index later
    intStudentId = int(studentId)
    
    #student - list, where contains all info about student with this studentId
    student = listOfStudents[intStudentId-1]

    #listOfMyModules - list, where contains all modules that student with this studentId takes
    listOfMyModules = student[len(student)-2]

    print('All your modules:')
    #with loop we show all modules that student takes
    for i in range(len(listOfMyModules)):
        print(i+1, ')', listOfMyModules[i])
        
    studentModuleChoice = input('Pick a number from what module do you want to unenroll')
    
    if(len(listOfMyModules)==0):
        print('You havent taken a module yet')
        return Student(studentId)
    #isdigit() check if the string input that have all characters in it as a digits. Also we check here so user would choose number in given range
    elif(studentModuleChoice.isdigit() and int(studentModuleChoice)>0 and int(studentModuleChoice)<=len(listOfMyModules)):
        #chosenModule - name of module which student choose to unenroll from
        chosenModule = listOfMyModules[int(studentModuleChoice)-1]
        
        #we remove module from student module list
        listOfMyModules.remove(chosenModule)

        for i in range(len(listOfCourses)):
            if(listOfCourses[i][0]==chosenModule):
                #course - list, where 1 element - subject name, 2 element - list of students for course
                course = listOfCourses[i]
                listOfStudentsForSubject = course[1]
                #we find student by studentId to delete him from module
                for k in range(len(listOfStudentsForSubject)):
                    if(studentId==listOfStudentsForSubject[k][0]):
                        listOfStudentsForSubject.pop(k)
                        break
                #replace the old version with updated
                course[1] = listOfStudentsForSubject
                listOfCourses[i] = course
                break
            
        updateListInTextFile()
        print('You have successfuly deleted yourself from module')
        return Student(studentId)
    else:
        print('Incorrect input')
        return unenrollInModule(studentId)


def loginStudent():
    studentId = input('Enter student ID')
    flag = checkIfStudentIdExist(studentId)
    if(flag==True):
        return Student(studentId)
    print('Student with this id doesnt exist')
    return loginStudent()

def Student(studentId):
    print(listOfStudents[int(studentId)-1][1])
    print('Choose number from 1-5 what action do you want choose')
    print('1 - view all available modules')
    print('2 - view my grades')
    print('3 - view my attendance')
    print('4 - enrol in module')
    print('5 - unenrol in module')
    print('6 - exit')
    userInput = input()
    if(userInput=='1'):
        return viewAllAvailableModules(studentId)
    elif(userInput=='2'):
        return viewOwnGrades(studentId, 'Student')
    elif(userInput=='3'):
        return viewAttendance(studentId)
    elif(userInput=='4'):
        return enrolInModule(studentId, 'Student')
    elif(userInput=='5'):
        return unenrollInModule(studentId)
    elif(userInput=='6'):
        return start()
    else:
        return Student(studentId)
    print('Student with this StudentID doesnt exist')   
    return start()            


################################################################################################# REGISTRAR
        
#check if studentID exist
def checkIfStudentIdExist(studentId):
    for item in listOfStudents:
        if(item[0]==studentId):
            return True
    return False

#Registering new student
def registerNewStudent(typeOfUser):
    newStudentFirstName = input('Please enter first name of the new student')
    if not newStudentFirstName.isalpha():
        print('Write letters for name')
        return registerNewStudent(typeOfUser)
    
    newStudentLastName = input('Please enter last name of the new student')
    if not newStudentLastName.isalpha():
        print('Write letters for name')
        return registerNewStudent(typeOfUser)

    newStudentNumber = input('Please enter your contact number')
    if(not newStudentNumber.isdigit()):
        print('Incorrect input for phone number')
        return registerNewStudent(typeOfUser)

    newStudentId = str(int(listOfStudents[-1][0])+1)
    if(len(newStudentId)==1):
        newStudentId='00'+newStudentId
    else:
        newStudentId='0'+newStudentId
        
    listOfStudents.append([newStudentId, newStudentFirstName+' '+newStudentLastName, 'APD-24-09-SE', newStudentNumber, [], [7900, False]])
    print('You have successfully registred new student')
    updateListInTextFile()
    if(typeOfUser=='Admin'):
        return Administartor()
    elif(typeOfUser=='Register'):
        return Register()
        


#Updating contact number of user
def updateStudentNumber():
    studentId = input('Enter student ID')
    flag = checkIfStudentIdExist(studentId)
    if(flag==True):
        newStudentNumber = input('Please enter new student contact number')
        if(newStudentNumber.isdigit() and int(newStudentNumber)!=0):
            intStudentId = int(studentId)
            listOfStudents[intStudentId-1][3] = newStudentNumber
            print('Your number was updated!')
            print(listOfStudents)
            updateListInTextFile()
            return Register()
        else:
            print('Number should consist on. Try again')
            return updateStudentNumber()
    else:
        print('Incorrect input or student doesnt exist. Try again')
        return updateStudentNumber()
    

#Issue transcript
def issueTranscript():
    studentId = input('Please enter id of the student')
    flag = checkIfStudentIdExist(studentId)
    if(flag==True):
        return viewOwnGrades(studentId, 'Register')
    print('Incorrect input or student doesnt exist. Try again')
    return issueTranscript()

def viewStudentInformation():
    studentId = input('Please enter id of the student')
    flag = checkIfStudentIdExist(studentId)
    if(flag==True):
        print(listOfStudents[int(studentId)-1])
        return Register()
    print('Incorrect input or student doesnt exist. Try again')
    return viewStudentInformation()


def manageEnrollmentForStudent():
    studentId = input('Please enter id of the student')
    flag = checkIfStudentIdExist(studentId)
    if(flag==True):
        return enrolInModule(studentId, 'Register')
    print('Incorrect input or student doesnt exist. Try again')
    return manageEnrollmentForStudent()

def Register():
    print('Which operation you would like to choose?')
    print('1. Register new student')
    print('2. Update contact number of the student')
    print('3. Enroll student in module')
    print('4. Issue transcripts')
    print('5. View information about student')
    print('6. Exit')
    userInput = input()
    if(userInput=='1'):
        return registerNewStudent('Register')
    elif(userInput=='2'):
        return updateStudentNumber()
    elif(userInput=='3'):
        return manageEnrollmentForStudent()
    elif(userInput=='4'):
        return issueTranscript()
    elif(userInput=='5'):
        return viewStudentInformation()
    elif(userInput=='6'):
        return start()
    else:
        return Register()        
        


###########################################################################################################   ACCOUNTANT 

def login_check():
    login = input('Please enter your login: ')
    password = input('Please enter your password: ')
    return [login, password]



        
def accountant():
        print(' ----------------------------------')
        print('What would you like to do next?')
        ch2 = input('1. View all payment fees \n2. View outstanding fees \n3. Update/Check information of student \n4. View summary \n5. Back \n')
        if ch2 == '5':
            return start()
        if ch2 == '1':
            view()
        elif ch2 == '2':
            view_out()
        elif ch2 == '3':
            upd_fee()
        elif ch2 == '4':
            summary()
        else:
            print('Please try again, choose the number of your choice')
            return accountant()
        
def view():
    print(' ----------------------------------')
    print('Here is the list of all students with their fees:')
    for i in range(len(listOfStudents)):
        print(f'ID:{listOfStudents[i][0]} {listOfStudents[i][1]} {listOfStudents[i][5]}')
    return accountant()

def view_out():
    print(' ----------------------------------')
    print('Here is the list of all students with outstanding fees:')
    for i in range(len(listOfStudents)):
        if listOfStudents[i][5][1] == False:
            print(f'ID:{listOfStudents[i][0]} {listOfStudents[i][1]} {listOfStudents[i][5]}')
    return accountant()

def upd_fee():
    stud = input("If you want to go back type BACK \nWhich student's information you need to get? \nPlease enter his ID: ")
    if stud == 'BACK':
        return accountant()
    found = False
    for i in range(len(listOfStudents)):
        if stud == listOfStudents[i][0]:
            found = True
            upd(i)
    if not found:
        print('Student with that ID not found, please try again')
        return upd_fee()
    
    
def upd(i):
    upd_choice = input('What do you want to update? \n1. Payment amount \n2. Fee status \n3. Get the fee recipe \n')
    if upd_choice == '1':        
        new_amount = input('Please enter the new amount of payment: ')
        if new_amount.isnumeric() and float(new_amount) >= 0:
            listOfStudents[i][5][0] = float(new_amount)
            updateListInTextFile()
        else:
            print('Incorrect amount of payment, please try again')
            return upd(i)
    elif upd_choice == '2':
        new_status = input('Please enter the status of payment (True or False): ')
        if new_status == 'True':
            listOfStudents[i][5][1] = True
            updateListInTextFile()
        elif new_status == 'False':
            listOfStudents[i][5][1] = False
            updateListInTextFile()
        else:
            print("Invalid input. Please enter 'True' or 'False'.")
            return upd(i)
    elif upd_choice == '3':
        print(' ----------------------------------')
        if listOfStudents[i][5][1] == True:
            print(f"Here is the fee recipe of {listOfStudents[i][0], listOfStudents[i][1]}")
            print(f"Paid: {listOfStudents[i][5][0]}")
            print(' ----------------------------------')
        else:
            print("This student didn't pay the fees, choose another one")
            print(' ----------------------------------')
            return upd_fee()
    else:
        print('Please try again, choose the number of your choice')
        return upd(i)
    
    return accountant()

def summary():
    summary_paid = 0
    summary_out = 0
    for i in range(len(listOfStudents)):
        if listOfStudents[i][5][1] == True:
            summary_paid = summary_paid + listOfStudents[i][5][0]
        else:
            summary_out = summary_out + listOfStudents[i][5][0]
    print(' ----------------------------------')
    print(f"Summary of paid fees: {summary_paid}")
    print(f"Summary of outstanding fees: {summary_out}")
    return accountant()


############################################################################   LECTURER

#loops through the list of lecturers and shows which module each is assigned to 

def viewAssignedModules():
    for singleLec in listOfLectures:
        print(singleLec[0],"is in charge of",singleLec[2])


#changes the student grade or attendance depending on the arguments provided 
#one of the last two has to have a value of None when using the method
 
def changeStudentModuleRecord(module,studentID,grade=None,attendance=None):
    moduleStudentGrades = []

    for course in listOfCourses:
        if course[0] == module:
            moduleStudentGrades = course[1]

    for studentGrade in moduleStudentGrades:
                if studentGrade[0] == studentID:
                    if(grade != None):
                        studentGrade[1] = grade
                    else:
                        studentGrade[2] = attendance
    updateListInTextFile()


#shows the list of students who are enrolled to the module provided as the argument as well as their student ID's
 
def showEnrolledStudentsForModule(module): 
    #print(listOfStudents)
    enrolledStudentIDs = []
    enrolledStudentNames = []
    for course in listOfCourses:
        if course[0] == module:
            for enrolledStudent in  course[1]:
                    enrolledStudentIDs.append(enrolledStudent[0])

    for studentID in enrolledStudentIDs:
        for student in listOfStudents:
            if studentID == student[0]:
                studentDetails = ("Name: %s | Student ID: %s" % (student[1],student[0]))
                enrolledStudentNames.append(studentDetails) 

    for studentName in enrolledStudentNames:
        print(studentName)


def getListOfForYourSubject(module):
    enrolledStudentIDs = []
    enrolledStudentNames = []
    for course in listOfCourses:
        if course[0] == module:
            for enrolledStudent in  course[1]:
                    enrolledStudentIDs.append(enrolledStudent[0])

    for studentID in enrolledStudentIDs:
        for student in listOfStudents:
            if studentID == student[0]:
                studentDetails = student[0]
                enrolledStudentNames.append(studentDetails) 

    return enrolledStudentNames

#shows the students names, their grades for a module as well as their attendance when passed their lecturersName as an argument

def viewStudentGrades(lecturerName):
    moduleName = ""
    studentIDsandGrades = []
    studentGradesAndNames = []
    copyOfListOfStudents = listOfStudents

    for lecturer in listOfLectures:
        if lecturer[0] == lecturerName:
            moduleName = lecturer[2]

    for course in listOfCourses:
        if course[0] == moduleName:
            for enrolledStudents in course[1]:
                studentIDsandGrades.append([enrolledStudents[0],enrolledStudents[1],enrolledStudents[2]])

    for student in copyOfListOfStudents:
        for studentGradeAndID in studentIDsandGrades:
            if student[0] == studentGradeAndID[0]:
                newStudentGradeAndName = "%s has a score of %s and an attendance record of %s" % (student[1],studentGradeAndID[1],studentGradeAndID[2]) 
                studentGradesAndNames.append(newStudentGradeAndName)
                studentGradeAndID[0] = None

    for studentGradeAndName in studentGradesAndNames:
        print(studentGradeAndName)


#takes a lectuers names, finds their details in the data file and stores their data as a variable 

def getLecObject():
    count = 0
    while True:
        if count == 0:    
                name = input("Please enter your full name: ")
        else:
                print("A lecturer with that name does not exist, please try again")
                print("")
                name = input("Please enter your full name: ")
        for lc in listOfLectures:
            if lc[0] == name:
                return lc
        count += 1

#verifies that the lecturers password is correct before allowing them to access the applications functionalities

def authenticateLec():
    count = 0
    while True:
        if count == 0:
            print("")
            passWord = input("Please enter your password to complete the login: ")
            print("")
        else:
            print("Invalid password, please try again")
            passWord = input("Please enter your password to complete login: ")
        for lec in listOfLectures:
            if lec[1] == passWord:
                print("You have successfully been logged in ")
                print("")
                return



#validates that the grade data type entered is appropriate and that it is within a valid range

def gradeValidator(grade):
    #grade must be a float/int between 0 and 100
    try:
        grade = int(grade)
        if(grade < 0):
            print("")
            print("The grade cannot be less than 0")
            print("")
            return False
        if(grade > 100):
            print("")
            print("The grade cannot be greator than 100")
            print("")
            return False
        return True
    except:
        print("")
        print("Invalid grade, the grade must be a whole number")
        print("")
        return False
    

#validates that the attendance data type entered is appropriate and that it is within a valid range

def attendanceValidator(attendance):
    try:
        attendance = int(attendance)
        if(attendance < 0):
            print("")
            print("The attendance cannot be less than 0")
            print("")
            return False
        if(attendance > 100):
            print("")
            print("The attednace cannot be greator than 100")
            print("")
            return False
        return True
    except:
        print("")
        print("Invalid attendance value, the attendance must be a whole number")
        print("")
        return False
    
#provides an interface between the application business logic 
#for changing the students attendance/grade and the user interface 

def changeStudentsPerfomanceInUI(perfomanceRecordType,lecObject):
        listOfStudentsForModule = getListOfForYourSubject(lecObject[2])
        while True:
                count = 0
                updatedGrade = False
                if count == 0:
                    count+=1
                    print("")
                    stdID = input("Please enter a student's ID from the list to continue: ")
                    print("")
                    if perfomanceRecordType == "grade":
                        for stud in listOfStudents:
                            if stud[0] == stdID and stdID in listOfStudentsForModule:
                                newGrade = input("Please enter the new student grade: ")
                                print("")
                                while gradeValidator(newGrade) == False:
                                    newGrade = input("Please enter the new student grade: ")
                                    print("")
                                changeStudentModuleRecord(lecObject[2],stdID,newGrade,None)
                                print("Here are the updated records: ")
                                print("")
                                viewStudentGrades(lecObject[0])
                                updatedGrade = True
                                break
                    elif perfomanceRecordType == "attendance":
                        for stud in listOfStudents:
                            if stud[0] == stdID and stdID in listOfStudentsForModule:
                                newAttendance = input("Please enter the new student attendance: ")
                                newAttendance = newAttendance.replace("%","")
                                while attendanceValidator(newAttendance) == False:
                                    newAttendance = input("Please enter the new student attendance: ")
                                    newAttendance = newAttendance.replace("%","")
                                newAttendance = newAttendance + "%"
                                changeStudentModuleRecord(lecObject[2],stdID,None,newAttendance)
                                print("")
                                print("Here are the updated records: ")
                                print("")
                                viewStudentGrades(lecObject[0])
                                updatedGrade = True
                                break
                if updatedGrade == True:
                    break



#provides an interface between the application business logic and the user interface 

def functionalityInUI(userChoiceInt, lecObject):
    if userChoiceInt == 1:
        print("")
        print("Here are the lecturers and the modules they are assigned to:")
        print("")
        viewAssignedModules()
        return lecMenu()
    elif userChoiceInt == 2:
        print("")
        print("Here are the students currently enrolled in your module: ")
        print("")
        showEnrolledStudentsForModule(lecObject[2])
        changeStudentsPerfomanceInUI("grade",lecObject)
        return lecMenu()
    elif userChoiceInt == 3:
        print("")
        print("Here are the students currently enrolled in your module: ")
        print("")
        showEnrolledStudentsForModule(lecObject[2])
        return lecMenu()
    elif userChoiceInt == 4:
        print("")
        print("Here are the students currently enrolled in your module: ")
        print("")
        showEnrolledStudentsForModule(lecObject[2])
        changeStudentsPerfomanceInUI("attendance",lecObject)
        return lecMenu()
    elif userChoiceInt == 5:
        print("")
        viewStudentGrades(lecObject[0])
        return lecMenu()
    

#provides the user interface to the application user 

def lecMenu(lecObject):
    while True:
        print("")
        print("Here are the available functionalities...")
        print("")
        print("1. View assigned modules")
        print("2. Add or update the grade of a student in your module")
        print("3. View the students currently enrolled in your module")
        print("4. Add or update the attendance of a student in your module")
        print("5. View the perfomance of the students in your module")
        print("6. Exit the application")
        print("")
        choice = input("Select a number from the list and press enter to continue ")
        try:
            choice = int(choice)
            if(choice < 1 or choice > 6):
                print("Invalid choice, please try again")
                print("")
            elif choice != 6:
                return functionalityInUI(choice,lecObject)
            else:
                return start()
        except:
            print("")

def lecturerFunctionalitiesEntryPoint():
    lecObject = getLecObject()
    authenticateLec()
    lecMenu(lecObject)

###########################################################################  Admin

def Administartor():
    
    command = ""

    print("Administrator menu\n"
          "1 - Add new course\n"
          "2 - Remove course\n"
          "3 - Add new student\n"
          "4 - Remove student\n"
          "5 - View data\n"
          "6 - Exit\n")

    command = input("Please enter a number which corresponds to a command: ")

    if command == '1':
        add_new_course()

    elif command == '2':
        remove_course()

    elif command == '3':
        registerNewStudent('Admin')

    elif command == '4':
        remove_student()

    elif command == '5':
        view_data()

    elif command == "6":
        return start()

    else:
        print("\nThere is no such command\n")
        return Administartor()

#adding new course to the course list


def add_new_course():
        flag = 0
        while not flag:
            flag2 = 1
            lecturer_name = input('Please enter new lecturers first name: ')
            if not lecturer_name.isalpha():
                print('Write letters for name')
                flag2 = 0
                return add_new_course()
            lecturer_surname = input('Please enter new lecturers last name: ')
            if not lecturer_surname.isalpha():
                print('Write letters for name')
                flag2 = 0
                return add_new_course()
            course_name = input("Please enter the name of the course: ").lower()
            for course in listOfCourses:
                if course[0] == course_name:
                    print('\nThis course already exists\n')
                    flag2 = 0
                    break
            if flag2:
                flag = 1

        listOfCourses.append([course_name,[]])
        listOfLectures.append([lecturer_name.capitalize()+' '+lecturer_surname.capitalize(), 'password', course_name])
        updateListInTextFile()
        print(f"\nCourse {course_name} was added successfully\n")
        return Administartor()

#removing course from the course list

def remove_course():
        flag = 0
        which_course = input("Please enter the name of the course to delete: ").lower()
        for i in range(len(listOfCourses)):
            if listOfCourses[i][0].lower() == which_course:
                for student in listOfStudents:
                    for course in student[4]:
                        if course.lower() == which_course:
                            student[4].remove(course)
                            break

                for lecturer in listOfLectures:
                    if lecturer[-1].lower() == which_course:
                        listOfLectures.remove(lecturer)

                flag = 1
                del listOfCourses[i]
                updateListInTextFile()
                print(f'\nCourse {which_course} was deleted successfully\n')
                break
        if not flag:
            print("\nCourse not found, please try again\n")
        
        return Administartor()




#Removing student from the student list

def remove_student():
        flag = 0
        student_id = input("Please enter the ID of student to delete: ")
        for i in range(len(listOfStudents)):
            if listOfStudents[i][0] == student_id:
                flag = 1
                print(f'\nStudent with id {student_id} was deleted successfully\n')

                for course in listOfCourses:
                    for student in course[1]:
                        if student[0] == student_id:
                            course[1].remove(student)

                del listOfStudents[i]
                updateListInTextFile()
                break
        if not flag:
            print("\nStudent not found, please try again\n")
        
        return Administartor()

#Viewing all the available data

def view_data():
        print(f"\nList of students: {listOfStudents}\n"
              f"List of lecturers: {listOfLectures}\n"
              f"List of courses: {listOfCourses}\n"
              f"List of admins: {admin}\n"
              f"List of registers: {listOfRegistrars}\n"
              f"List of accountants: {listOfAccountant}\n")

        return Administartor()


start()
    
