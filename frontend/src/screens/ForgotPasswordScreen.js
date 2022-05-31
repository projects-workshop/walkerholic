import axios from 'axios'
import React, { useState } from 'react'

function ForgotPasswordScreen() {

    const [email, setEmail] = useState('')
    const [load, setLoad] = useState(false)
    const [err, setErr] = useState('')
    const [success, setSuccess] = useState('')

    const handleSubmit = async(e) =>{
        e.preventDefault()
        setErr('')
        setSuccess('')
        if(email){
            setLoad(true)
            await axios.post(`/user/forgotPassword/${email}`)
                        .then(res=>{setSuccess(res.data);setEmail('');})
                        .catch(error=> setErr(error.response.data.message))
            setLoad(false)
        }else{
            window.alert('Please enter your email.')
        }
        
    }

    return (
        <>
            <div className="auth">
                <form onSubmit={handleSubmit}>
                    <div className="auth_message">
                        Forget your password?
                    </div>
                    {
                        err?
                        <div style={{color:"#bf3434", fontWeight:"800"}}>{err}</div>
                        :success?
                            <div style={{color:"#5ea871", fontWeight:"800"}}>{success}</div>
                            : <div> Please enter your email to receive new password.</div>
                    }
                    <div className="form_group">
                        <input type="email" className="form_control" value={email} onChange={e=>setEmail(e.target.value)}/>
                        <button>Send</button>
                    </div>
                </form>
            </div>
            {
                load &&
                <div>loading...</div>
            }
        </>
    )
}

export default ForgotPasswordScreen
