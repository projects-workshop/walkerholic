import React, { useState } from 'react'

function Error({error}) {
    const [showErr, setShowErr] = useState(true)
    return (
        <div>
            {
                showErr &&
                <div className="error">
                        <div className="error_form">
                            <div className="error-title">
                                <div>
                                    Error
                                </div>
                                <div onClick={()=>setShowErr(false)} style={{cursor:"pointer"}}>
                                    &times;
                                </div>
                            </div>
                            <hr/>
                            <div className="error-content">
                                {error}
                            </div>
                        </div>
                </div>
            }
        </div>
    )
}

export default Error