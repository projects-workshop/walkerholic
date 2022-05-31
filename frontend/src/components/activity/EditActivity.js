import React, { useState } from 'react'
import Earth from '../../images/earth.svg'
import { checkProfileImage } from '../../utils/CheckImage'
import { checkEditActivityFormValid} from '../../utils/CheckFormValid'
import { useDispatch } from 'react-redux'
import { createActivity, updateActivity } from '../../_actions/ActivityActions'



function EditActivity({activity, setIsEdit}) {

    const [name, setName] = useState(activity? activity.name : '')
    const [score, setScore] = useState(activity? activity.score : 0)
    const [description, setDescription] = useState(activity? activity.description : '')
    const [imageUrl, setImageUrl] = useState(activity? activity.imageUrl : '')

    const [err, setErr] = useState({})

    const dispatch = useDispatch()

    const changeActivityImage = (e) =>{
        const file = e.target.files[0]
        const err = checkProfileImage(file)
        if(err) return window.alert(err)
        if(file){
            var preview = document.getElementById('preview')
            preview.src = URL.createObjectURL(file)
        }
        setImageUrl(file)
    }

    const handleSubmit = (e) =>{
        e.preventDefault();

        const check = checkEditActivityFormValid(name, score, description)

        if(check.errLength>0) {
            setErr(check.err)
            return
        }

        const activityRequest = {
            name,
            score,
            description
        }

        if(activity){
            dispatch(updateActivity(activityRequest, activity.id, imageUrl)).then(res=>{
                setIsEdit(false)
            })
        }else{
            dispatch(createActivity(activityRequest, imageUrl)).then(res=>{
                setIsEdit(false)
            })
        }
    }


    return (
        <div className="edit_profile">
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                <div className="auth_message">
                        {
                            activity
                            ? "Edit Activity"
                            : "Create Activity"
                        }
                </div>
                <div style={{width:"100%", height:"150px" , display:"flex", justifyContent:"center", marginTop:"2rem", marginBottom:"2rem"}}>
                    <div className="form_group_image" >
                        <img id="preview" src={(activity && activity.imageUrl)? activity.imageUrl : Earth} alt="profileImage" />
                        <span>
                            <i className="far fa-images" ></i>
                            <input type="file" name="file" id="file_up" accept="image/*" onChange={changeActivityImage}/>
                        </span>
                    </div>
                </div>

                <div className="form_group">
                    <label htmlFor="name">Name</label>
                    <input type="text" className="form_control" value={name} onChange={e=>setName(e.target.value)} />
                </div>
                {
                    err.name
                    ? <small>{err.name}</small>
                    :''
                }

                <div className="form_group">
                    <label htmlFor="score">Score</label>
                    <input type="Number" className="form_control" value={score} onChange={e=>setScore(e.target.value)} />
                </div>
                {
                    err.score
                    ? <small>{err.score}</small>
                    :''
                }

                <div className="form_group">
                    <label htmlFor="description">Description</label>
                    <textarea type="text" className="form_control" cols="32" value={description} onChange={e=>setDescription(e.target.value)} />
                </div>
                {
                    err.description
                    ? <small>{err.description}</small>
                    :''
                }

                <div className="form_button" style={{marginTop:"1.3rem"}}>
                    <button className="follow_button" style={{marginRight:"1rem"}} type="submit">Save</button>
                    <button className="unfollow_button" onClick={()=>setIsEdit(false)}>Cancel</button>
                </div>
            </form>
        </div>
    )
}

export default EditActivity
