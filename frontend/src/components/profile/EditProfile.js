import React from 'react'
import { useState } from 'react'
import { checkProfileImage } from '../../utils/CheckImage'
import { checkEditProfileFormValid } from '../../utils/CheckFormValid'
import Earth from '../../images/earth.svg'
import { useDispatch, useSelector } from 'react-redux'
import { editProfile } from '../../_actions/ProfileActions'
import axios from 'axios'
import { GET_AUTH_FOLLOWS } from '../../_constants/AuthConstants'



function EditProfile({setIsEdit}) {

    const auth = useSelector(state => state.auth)

    const [lastname, setLastname] = useState(auth.user.lastname)
    const [firstname, setFirstname] = useState(auth.user.firstname)
    const [email, setEmail] = useState(auth.user.email)
    const [password, setPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')
    const [description, setDescription] = useState(auth.user.description? auth.user.description:'')
    const [phoneNumber, setPhoneNumber] = useState(auth.user.phoneNumber? auth.user.phoneNumber :'')
    const [isSeller, setIsSeller] = useState(auth.user.seller)
    const [imageUrl, setImageUrl] = useState(auth.user.imageUrl?auth.user.imageUrl :null)

    const [typePass, setTypePass] = useState(false)
    const [cpTypePass, setCpTypePass] = useState(false)

    const [err, setErr] = useState({})

    const dispatch = useDispatch()

    const changeProfileImage = async(e) =>{
        const file = e.target.files[0]
        const err = checkProfileImage(file)
        if(err) return window.alert(err)
        if(file){
            var preview = document.getElementById('preview')
            preview.src = URL.createObjectURL(file)
        }
        setImageUrl(file)
    }


    const handleSubmit = (e)=>{
        e.preventDefault();

        const check = checkEditProfileFormValid(lastname,firstname,email,password, confirmPassword, description, phoneNumber)

        if(check.errLength>0) {
            setErr(check.err)
            return
        }

        const bodyFormData = new FormData()
        bodyFormData.append('id', auth.user.id)
        bodyFormData.append('firstname', firstname)
        bodyFormData.append('lastname', lastname)
        bodyFormData.append('email', email)
        bodyFormData.append('password', password)
        bodyFormData.append('phoneNumber', phoneNumber)
        bodyFormData.append('description', description)
        bodyFormData.append('isSeller', isSeller)
        bodyFormData.append('multipartFile', imageUrl)

        dispatch(editProfile(bodyFormData)).then(async(id)=>{
            const res = await axios.get(`/follows/${id}`,{
                headers : {Authorization : `Bearer ${auth.token}`}
            })
            dispatch({
              type:GET_AUTH_FOLLOWS,
              payload:res.data
            })
            setIsEdit(false)
          })

    }
    return (
        <div className="edit_profile"> 
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                <div className="auth_message">
                        Edit Your Information
                </div>

                <div style={{width:"100%", height:"150px" , display:"flex", justifyContent:"center", marginTop:"2rem", marginBottom:"2rem"}}>
                    <div className="form_group_image" >
                        <img id="preview" src={imageUrl ? imageUrl : Earth} alt="profileImage" />
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
                    <label htmlFor="isSeller">Want to be a Seller ? </label>
                    <input type="checkbox" onClick={()=>setIsSeller(!isSeller)} />
                </div>

                <div className="form_button" style={{marginTop:"1.3rem"}}>
                    <button className="follow_button" style={{marginRight:"1rem"}} type="submit">Save</button>
                    <button className="unfollow_button" onClick={()=>setIsEdit(false)}>Cancel</button>
                </div>

            </form>
        </div>
    )
}

export default EditProfile
