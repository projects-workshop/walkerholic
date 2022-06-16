import React from 'react'
import { useEffect } from 'react'
import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { createUserActivity, editUserActivity, getActivities } from '../../_actions/ActivityActions'
import { LEVEL_UP } from '../../_constants/AuthConstants'

function EditUserActivity({activity, setIsEdit, isEdit}) {

    const [activityId, setActivityId] = useState(activity? activity.activityId : 1)
    const [finished, setFinished] = useState(activity? activity.finished : false)

    const activities = useSelector(state => state.activity.activities)
    const auth = useSelector(state => state.auth)

    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(getActivities())
    }, [dispatch])

    const handleSubmit = (e) =>{
        e.preventDefault()

        if(activity){
            const userActivityRequest = {
                userId:auth.user.id,
                activityId:activityId,
                finished:finished
            }
            dispatch(editUserActivity(userActivityRequest, activity.id)).then(res=>{
                if(res !== auth.user.level){
                    dispatch({
                        type:LEVEL_UP,
                        payload:res
                    })
                }
            })
        }else{
            const userActivityRequest = {
                userId:auth.user.id,
                activityId:activityId,
                finished:finished
            }
            dispatch(createUserActivity(userActivityRequest)).then(res=>{
                if(res !== auth.user.level){
                    dispatch({
                        type:LEVEL_UP,
                        payload:res
                    })
                }
            })
        }

        setIsEdit(false)
    }
    return (
        <div className="edit_post">
            <form onSubmit={handleSubmit} encType="multipart/form-data">

                {
                    activity
                    ? <div className="edit_title">Edit Activity</div>
                    : <div className="edit_title">Add Activity</div>
                }

                <div className="form_group">  
                  
                    <label htmlFor="activity">Activity</label>
                    {
                        activity
                        ? <div>{activity.activityName} ({activity.score})</div>
                        :<select value={activityId} onChange={e=>{setActivityId(e.target.value)}}>
                            {
                                activities?.map((activity,index)=>(
                                    <option value={activity.id} key={index}>{activity.name} ({activity.score})</option>
                                ))
                            }
                        </select>
                    }
                </div>

                <div className="form_group">    
                    <label htmlFor="isFinished">Complete</label>
                    <input type="checkbox" onClick={()=>setFinished(!finished)} value={finished}/>
                </div>

                <div className="form_button">
                    <button type="submit" className="follow_button" style={{marginRight:"1rem"}}>Save</button>
                    <button onClick={()=>setIsEdit(false)} className="unfollow_button">Cancel</button>
                </div>
            </form>
        </div>
    )
}

export default EditUserActivity
