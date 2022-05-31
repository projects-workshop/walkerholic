import React from 'react'
import { useState } from 'react'
import {Link,useHistory} from 'react-router-dom'
import { checkProfileImage } from '../utils/CheckImage'
import { checkRegisterFormValid } from '../utils/CheckFormValid'
import Earth from '../images/earth.svg'
import { useDispatch, useSelector } from 'react-redux'
import { register } from '../_actions/AuthActions'
import { useEffect } from 'react'
import Error from '../components/Error'
import Loading from '../components/Loading'


function RegisterScreen() {

    const [lastname, setLastname] = useState('')
    const [firstname, setFirstname] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')
    const [description, setDescription] = useState('')
    const [phoneNumber, setPhoneNumber] = useState('')
    const [isSeller, setIsSeller] = useState(false)
    const [imageUrl, setImageUrl] = useState('')

    const [typePass, setTypePass] = useState(false)
    const [cpTypePass, setCpTypePass] = useState(false)

    const [err, setErr] = useState({})

    const auth = useSelector(state => state.auth)
    const history = useHistory()

    const dispatch = useDispatch()

    useEffect(() => {
        if(auth.user && auth.user.id){
            history.push('/')
        }
    }, [auth.user])
    
    
    const changeProfileImage = (e) =>{
        const file = e.target.files[0]
        const err = checkProfileImage(file)
        if(err) return window.alert(err)
        if(file){
            var preview = document.getElementById('preview')
            preview.src = URL.createObjectURL(file)
        }
        console.log(file)
        setImageUrl(file)
    }


    const handleSubmit = (e)=>{
        e.preventDefault();

        const check = checkRegisterFormValid(lastname,firstname,email,password, confirmPassword, description, phoneNumber)

        if(check.errLength>0) {
            setErr(check.err)
            return
        }

        const bodyFormData = new FormData()
        bodyFormData.append('firstname', firstname)
        bodyFormData.append('lastname', lastname)
        bodyFormData.append('email', email)
        bodyFormData.append('password', password)
        bodyFormData.append('phoneNumber', phoneNumber)
        bodyFormData.append('description', description)
        bodyFormData.append('isSeller', isSeller)
        bodyFormData.append('multipartFile', imageUrl)

        dispatch(register(bodyFormData))
    }

    return (
        <div className="auth">
            {
                auth.error && auth.error.message && <Error error = {auth.error.message}/>
            }
            {
                auth.Loading && <Loading/>
            }

            <form onSubmit={handleSubmit} encType="multipart/form-data">
                <div className="auth_message" style={{marginBottom:"1rem"}}>
                        Be with us!
                </div>

                <div style={{width:"100%", height:"150px" , display:"flex", justifyContent:"center", marginBottom:"3rem"}}>
                    <div className="form_group_image" >
                        <img id="preview" src={Earth} alt="profileImage" />
                        <span>
                            <i className="far fa-images" ></i>
                            <input type="file" name="file" id="file_up" accept="image/*" onChange={changeProfileImage}/>
                        </span>
                    </div>
                </div>


                <div className="form_group">
                    <label htmlFor="lastname">Last Name</label>
                    <input type="text" className="form_control" value={lastname} onChange={e=>setLastname(e.target.value)} />
                </div>
                {
                    err.lastname
                    ? <small>{err.lastname}</small>
                    :''
                }

                <div className="form_group">
                    <label htmlFor="firstname">First Name</label>
                    <input type="text" className="form_control" value={firstname} onChange={e=>setFirstname(e.target.value)} />
                </div>
                {
                    err.firstname
                    ? <small>{err.firstname}</small>
                    :''
                }

                <div className="form_group">
                    <label htmlFor="email">Email</label>
                    <input type="email" className="form_control" value={email} onChange={e=>setEmail(e.target.value)}/>
                </div>
                {
                    err.email
                    ? <small>{err.email}</small>
                    :''
                }

                <div className="form_group">
                    <label htmlFor="password">Password</label>
                    <input type={typePass ? "text" :"password"} className="form_control" value={password} onChange={e=>setPassword(e.target.value)} />
                    <small className="pass" onClick={()=>setTypePass(!typePass)}>
                        {typePass ? 'Hide' : 'Show'}
                    </small>
                </div>
                {
                    err.password
                    ? <small>{err.password}</small>
                    :''
                }

                <div className="form_group">
                    <label htmlFor="confirmPassword">Confirm Password</label>
                    <input type={cpTypePass ? "text":"password"} className="form_control" value={confirmPassword} onChange={e=>setConfirmPassword(e.target.value)} />
                    <small className="pass" onClick={()=>setCpTypePass(!cpTypePass)}>
                        {cpTypePass ? 'Hide' : 'Show'}
                    </small>
                </div>
                {
                    err.confirmPassword
                    ? <small>{err.confirmPassword}</small>
                    :''
                }

                <div className="form_group">
                    <label htmlFor="phoneNumber">Phone Number</label>
                    <input type="text" className="form_control" value={phoneNumber} onChange={e=>setPhoneNumber(e.target.value)} />
                </div>
                {
                    err.phoneNumber
                    ? <small>{err.phoneNumber}</small>
                    :''
                }

                <div className="form_group">
                    <label htmlFor="description">Description</label>
                    <input type="text" className="form_control" value={description} onChange={e=>setDescription(e.target.value)} />
                </div>
                {
                    err.description
                    ? <small>{err.description}</small>
                    :''
                }

                <div className="form_group">
                    <label htmlFor="isSeller">Are you a Seller ? </label>
                    <input type="checkbox" onClick={()=>setIsSeller(!isSeller)} />
                </div>

                <div className="form_button" style={{margin:"2rem"}}>
                    <button>Sign up</button>
                </div>

                <div className="form_switch" style={{marginTop:"1rem"}}>
                    <div>Already have an acoount? </div>
                    <div style={{width:"100px"}}><Link to="/signin">Go For Login</Link></div>
                </div>

            </form>
        </div>
    )
}

export default RegisterScreen
