export const checkRegisterFormValid = (lastname, firstname, email, password, confirmPassword, description, phoneNumber)=>{
    const err={}

    if(!lastname) {
        err.lastname = "Please add your Last name."
    }else if(lastname.length>25){
        err.lastname = "Last name is up to 25characters long."
    }

    if(!firstname) {
        err.firstname = "Please add your first name."
    }else if(firstname.length>25){
        err.firstname = "First name is up to 25characters long."
    }

    if(!email){
        err.email = "Please add your email."
    }else if(!validateEmail(email)){
        err.email = "Email format is incorrect."
    }

    if(!password){
        err.password = "Please add your password."
    }else if(password.length<6){
        err.password = "Password must be at least 6 characters."
    }

    if(description.length>200){
        err.description="Description is up to 200characters long."
    }

    if(phoneNumber.length>15){
        err.phoneNumber="PhoneNumber is up to 15characters long."
    }
    
    if(password !== confirmPassword){
        err.confirmPassword = "Confirm password did not match."
    }

    return{
        err,
        errLength:Object.keys(err).length
    }
}

const validateEmail = (email)=>{
    const re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}



export const checkEditProfileFormValid = (lastname, firstname, email, password, confirmPassword, description, phoneNumber)=>{
    const err={}

    if(!lastname) {
        err.lastname = "Please add your Last name."
    }else if(lastname.length>25){
        err.lastname = "Last name is up to 25characters long."
    }

    if(!firstname) {
        err.firstname = "Please add your first name."
    }else if(firstname.length>25){
        err.firstname = "First name is up to 25characters long."
    }
    if(!email){
        err.email = "Please add your email."
    }else if(!validateEmail(email)){
        err.email = "Email format is incorrect."
    }

    if(password && password.length<6){
        err.password = "Password must be at least 6 characters."
    }

    if(description.length>200){
        err.description="Description is up to 200characters long."
    }

    if(phoneNumber && phoneNumber.length>15){
        err.phoneNumber="PhoneNumber is up to 15characters long."
    }
    
    if(password !== confirmPassword){
        err.confirmPassword = "Confirm password did not match."
    }

    return{
        err,
        errLength:Object.keys(err).length
    }
}

export const checkEditActivityFormValid = (name, score, description)=>{
    const err={}

    if(!name) {
        err.name = "Please add name."
    }else if(name.length>25){
        err.name = "Name is up to 25characters long."
    }

    if(!score){
        err.score = "Please add score."
    }else if(score>1000){
        err.score = "Score is up to 1000points."
    }

    if(!description){
        err.description = "Please add description."
    }else if(description.length<5){
        err.description = "Description is too short."
    }

    return{
        err,
        errLength:Object.keys(err).length
    }
}

export const checkAddressFormValid = (addressName, address, country, city, zipcode)=>{
    const err={}
    if(!addressName) {
        err.addressName = "Please add addressName."
    }else if(addressName.length>25){
        err.addressName = "AddressName is up to 25characters long."
    }

    if(!address){
        err.address = "Please add address."
    }else if(address.length>50){
        err.address = "Address is up to 50characters long."
    }

    if(!country){
        err.country = "Please add country."
    }

    if(!city){
        err.city = "Please add country."
    }

    if(!zipcode){
        err.zipcode = "Please add country."
    }else if(zipcode.length<5){
        err.zipcode = "ZipCode is too short."
    }
    
    return{
        err,
        errLength:Object.keys(err).length
    }
}